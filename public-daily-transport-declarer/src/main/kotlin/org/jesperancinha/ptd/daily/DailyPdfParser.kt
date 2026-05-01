package org.jesperancinha.ptd.daily

import org.openpdf.text.pdf.PdfReader
import org.openpdf.text.pdf.parser.PdfTextExtractor
import java.math.BigDecimal
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class DailyPdfParser {
    private val datePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val timePattern = Pattern.compile("[0-9]{2}:[0-9]{2}")
    private val costPattern = Pattern.compile("(€)( *[0-9]+,?[0-9]*)")
    private val transportTypePattern = Pattern.compile("(Tram|Bus|Trein|NS|Arriva|Qbuzz)", Pattern.CASE_INSENSITIVE)

    fun parse(fileUrl: URL): List<Segment> {
        val reader = PdfReader(fileUrl)
        val extractor = PdfTextExtractor(reader)
        val text = (1..reader.numberOfPages).joinToString("\n") { extractor.getTextFromPage(it) }
        
        return text.split("\n")
            .filter { isTransportLine(it) }
            .flatMap { parseLine(it) }
    }

    private fun isTransportLine(line: String): Boolean {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return false
        if (trimmed.contains("Pagina", ignoreCase = true)) return false
        if (trimmed.contains("voldoet aan de eisen", ignoreCase = true)) return false
        val firstWord = trimmed.split(" ").firstOrNull() ?: return false
        return try {
            LocalDate.parse(firstWord, datePattern)
            // Additional check: a transport line should have "Check-in" or "Check-uit"
            trimmed.contains("Check-in", ignoreCase = true) || trimmed.contains("Check-uit", ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }

    private fun parseLine(line: String): List<Segment> {
        val segments = mutableListOf<Segment>()
        val trimmed = line.trim()
        val parts = trimmed.split(Pattern.compile("\\s+"))
        if (parts.size < 2) return emptyList()

        val date = LocalDate.parse(parts[0], datePattern)
        
        val costMatcher = costPattern.matcher(line)
        val cost = if (costMatcher.find()) {
            BigDecimal(costMatcher.group(2).replace(",", ".").trim())
        } else {
            BigDecimal.ZERO
        }

        val type = when {
            line.contains("Tram", ignoreCase = true) ||  line.contains("Qbuzz", ignoreCase = true)  -> TransportType.TRAM
            line.contains("Bus", ignoreCase = true) -> TransportType.BUS
            line.contains("Trein", ignoreCase = true) || line.contains("NS", ignoreCase = true) || line.contains("Arriva", ignoreCase = true) -> TransportType.TRAIN
            else -> TransportType.OTHER
        }

        // Search for times in the line
        val times = mutableListOf<String>()
        val timeMatcher = timePattern.matcher(line)
        while (timeMatcher.find()) {
            times.add(timeMatcher.group())
        }

        if (times.size >= 2) {
            // Likely one line with both check-in and check-out
            // Format: Date Company Start Time End Station Cost Check-uit
            // Actually in the PDF: 01-12-2022 Qbuzz Nieuwegein, Nieuwegein City08:17Utrecht, CS Jaarbeursplein€ 2,64Check-uit
            // Wait, the time is ATTACHED to the start station or end station in the extractor?
            // "Nieuwegein City08:17Utrecht"
            
            // Let's re-examine: 01-12-2022 Qbuzz Nieuwegein, Nieuwegein City08:17Utrecht, CS Jaarbeursplein€ 2,64Check-uit
            // Time 08:17 is there.
            
            // If there's only one time but it's a "Check-uit", it might be that the check-in time is missing or we need to infer it.
            // But wait, "Check-uit" line usually means the cost is for the WHOLE journey.
        }

        val isCheckOut = line.contains("Check-uit", ignoreCase = true)
        
        if (isCheckOut) {
            // We'll treat this as a single "Journey" segment for now if it contains a cost.
            // In this specific PDF format, one line = one journey (check-in to check-out).
            // So we can create two segments: one for check-in (at unknown time/station) and one for check-out.
            // Or just create one segment and handle it specially.
            
            val dateTime = if (times.isNotEmpty()) {
                LocalDateTime.parse("${parts[0]} ${times[0]}", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
            } else {
                date.atStartOfDay()
            }
            
            segments.add(Segment(dateTime, extractStation(line), type, CheckInOut.CHECKOUT, cost))
        } else if (line.contains("Check-in", ignoreCase = true)) {
            val dateTime = if (times.isNotEmpty()) {
                LocalDateTime.parse("${parts[0]} ${times[0]}", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
            } else {
                date.atStartOfDay()
            }
            segments.add(Segment(dateTime, extractStation(line), type, CheckInOut.CHECKIN, cost))
        }

        return segments
    }

    private fun extractStation(line: String): String {
        // Find "€"
        val euroIndex = line.indexOf("€")
        if (euroIndex != -1) {
            // Work backwards from € to find the station
            val beforeEuro = line.substring(0, euroIndex).trim()
            // The station is usually after the company name and maybe after a time
            val timeMatcher = timePattern.matcher(beforeEuro)
            var lastTimeEnd = -1
            while (timeMatcher.find()) {
                lastTimeEnd = timeMatcher.end()
            }
            
            val stationPart = if (lastTimeEnd != -1) {
                beforeEuro.substring(lastTimeEnd).trim()
            } else {
                // If no time found, try skipping the date and company
                val parts = beforeEuro.split(Pattern.compile("\\s+"))
                if (parts.size > 2) {
                    parts.drop(2).joinToString(" ")
                } else {
                    beforeEuro
                }
            }
            // Clean up station part from any remaining info like transport type if it's there
            return stationPart.replace(transportTypePattern.pattern().toRegex(), "").trim()
        }
        return "Unknown"
    }
}

fun List<Segment>.toJourneys(): List<Journey> {
    val journeys = mutableListOf<Journey>()
    val sortedSegments = this.sortedBy { it.dateTime }
    
    val pendingCheckIns = mutableListOf<Segment>()
    
    for (segment in sortedSegments) {
        if (segment.check == CheckInOut.CHECKIN) {
            pendingCheckIns.add(segment)
        } else {
            // It's a Check-out.
            val lastCheckIn = pendingCheckIns.lastOrNull { it.type == segment.type && it.dateTime.toLocalDate() == segment.dateTime.toLocalDate() }
            if (lastCheckIn != null) {
                pendingCheckIns.remove(lastCheckIn)
                journeys.add(Journey(lastCheckIn, segment, segment.type))
            } else {
                // Check-out without a corresponding Check-in line in the PDF.
                // Create a dummy check-in
                val dummyCheckIn = Segment(segment.dateTime.minusMinutes(30), "Unknown", segment.type, CheckInOut.CHECKIN, BigDecimal.ZERO)
                journeys.add(Journey(dummyCheckIn, segment, segment.type))
            }
        }
    }
    
    // Remaining pending check-ins are incomplete journeys
    for (incomplete in pendingCheckIns) {
        journeys.add(Journey(incomplete, null, incomplete.type))
    }
    
    return journeys.sortedBy { it.checkIn.dateTime }
}
