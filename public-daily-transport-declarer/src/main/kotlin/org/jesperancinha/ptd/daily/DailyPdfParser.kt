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
    private val transportTypePattern = Pattern.compile("(Tram|Bus|Trein|NS|Arriva)", Pattern.CASE_INSENSITIVE)

    fun parse(fileUrl: URL): List<Segment> {
        val reader = PdfReader(fileUrl)
        val extractor = PdfTextExtractor(reader)
        val text = (1..reader.numberOfPages).joinToString("\n") { extractor.getTextFromPage(it) }
        
        return text.split("\n")
            .filter { isTransportLine(it) }
            .map { parseLine(it) }
    }

    private fun isTransportLine(line: String): Boolean {
        val firstWord = line.trim().split(" ").firstOrNull() ?: return false
        return try {
            LocalDate.parse(firstWord, datePattern)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun parseLine(line: String): Segment {
        val parts = line.trim().split(" ")
        val date = LocalDate.parse(parts[0], datePattern)
        val timeMatcher = timePattern.matcher(line)
        val time = if (timeMatcher.find()) timeMatcher.group() else "00:00"
        val dateTime = LocalDateTime.parse("${parts[0]} $time", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        
        val check = if (line.contains("Check-uit", ignoreCase = true)) CheckInOut.CHECKOUT else CheckInOut.CHECKIN
        
        val costMatcher = costPattern.matcher(line)
        val cost = if (costMatcher.find()) {
            BigDecimal(costMatcher.group(2).replace(",", ".").trim())
        } else {
            BigDecimal.ZERO
        }

        val type = when {
            line.contains("Tram", ignoreCase = true) -> TransportType.TRAM
            line.contains("Bus", ignoreCase = true) -> TransportType.BUS
            line.contains("Trein", ignoreCase = true) || line.contains("NS", ignoreCase = true) -> TransportType.TRAIN
            else -> TransportType.OTHER
        }

        // Station is usually after the time and before the transport type or check-in/out
        // This is a simplification, but let's try to extract something
        val station = extractStation(line)

        return Segment(dateTime, station, type, check, cost)
    }

    private fun extractStation(line: String): String {
        // Very simplified: everything between time and "Check-"
        val timeMatcher = timePattern.matcher(line)
        if (timeMatcher.find()) {
            val afterTime = line.substring(timeMatcher.end()).trim()
            val checkIndex = afterTime.indexOf("Check-")
            if (checkIndex != -1) {
                return afterTime.substring(0, checkIndex).trim()
            }
            return afterTime
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
            // Find the most recent check-in for the same transport type and same day?
            // Usually it's the last one.
            val lastCheckIn = pendingCheckIns.lastOrNull { it.type == segment.type && it.dateTime.toLocalDate() == segment.dateTime.toLocalDate() }
            if (lastCheckIn != null) {
                pendingCheckIns.remove(lastCheckIn)
                journeys.add(Journey(lastCheckIn, segment, segment.type))
            } else {
                // Check-out without check-in (should not happen often in clean data but handle it)
                // Treat as incomplete? Or just ignore? The requirement says failed checkout.
                // If we have a checkout but no checkin, it's also an error.
                // But the requirement says "where for example, a checkout failed", which usually means Check-in without Check-out.
            }
        }
    }
    
    // Remaining pending check-ins are incomplete journeys
    for (incomplete in pendingCheckIns) {
        journeys.add(Journey(incomplete, null, incomplete.type))
    }
    
    return journeys.sortedBy { it.checkIn.dateTime }
}
