package org.jesperancinha.ptd.daily

import org.openpdf.text.pdf.PdfReader
import org.openpdf.text.pdf.parser.PdfTextExtractor
import java.math.BigDecimal
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

private val HOUR_MINUTE_REGEX = Regex("[0-9]{2}:[0-9]{2}")

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

    internal fun parseLine(line: String): List<Segment> {
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
            line.contains("Tram", ignoreCase = true) || line.contains("Qbuzz", ignoreCase = true) || line.contains("Connexxion", ignoreCase = true) -> TransportType.TRAM
            line.contains("Bus", ignoreCase = true) -> TransportType.BUS
            line.contains("Trein", ignoreCase = true) || line.contains(
                "NS",
                ignoreCase = true
            ) || line.contains("Arriva", ignoreCase = true) -> TransportType.TRAIN

            else -> TransportType.OTHER
        }

        val times = mutableListOf<String>()
        val timeMatcher = timePattern.matcher(line)
        while (timeMatcher.find()) {
            times.add(timeMatcher.group())
        }

        val isCheckOut = line.contains("Check-uit", ignoreCase = true)

        if (isCheckOut) {
            val (departure, destination) = extractStations(line)
            val dateTime = if (times.isNotEmpty()) {
                LocalDateTime.parse("${parts[0]} ${times.last()}", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
            } else {
                date.atStartOfDay()
            }

            segments.add(Segment(dateTime, destination, type, CheckInOut.CHECKOUT, cost))
        } else if (line.contains("Check-in", ignoreCase = true)) {
            val dateTime = if (times.isNotEmpty()) {
                LocalDateTime.parse("${parts[0]} ${times[0]}", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
            } else {
                date.atStartOfDay()
            }
            segments.add(Segment(dateTime, extractCheckInStation(line), type, CheckInOut.CHECKIN, cost))
        }

        return segments
    }

    private fun extractCheckInStation(line: String): String {
        val timeMatch = HOUR_MINUTE_REGEX.find(line)
        if (timeMatch != null) {
            val timeIndex = timeMatch.range.first
            val beforeTime = line.substring(0, timeIndex).trim()
            val parts = beforeTime.split(Pattern.compile("\\s+"))
            if (parts.size >= 2) {
                return parts.drop(2).joinToString(" ").trim()
                    .replace(transportTypePattern.pattern().toRegex(), "").trim()
            }
            val afterTime = (line.substring(timeIndex + 5).trim() as String).replace("Check-in", "").split(" ").drop(1)
                .joinToString(" ")
            return afterTime.split("Reizen")[0].trim()
        }
        return "Unknown"
    }

    private fun extractStation(line: String): String {
        return extractStations(line).second
    }

    private fun extractStations(line: String): Pair<String, String> {
        // Find "€"
        val euroIndex = line.indexOf("€")
        if (euroIndex != -1) {
            // Work backwards from € to find the station
            val beforeEuro = line.substring(0, euroIndex).trim()
            // The station is usually after the company name and maybe after a time
            val timeMatcher = timePattern.matcher(beforeEuro)
            val times = mutableListOf<Pair<Int, Int>>()
            while (timeMatcher.find()) {
                times.add(timeMatcher.start() to timeMatcher.end())
            }

            if (times.isNotEmpty()) {
                val lastTime = times.last()
                val destination = beforeEuro.substring(lastTime.second).trim()
                    .replace(transportTypePattern.pattern().toRegex(), "").trim()

                val departure = if (times.size >= 1) {
                    val firstTime = times.first()
                    // Vertrek is before the first time found, but after date and company
                    val beforeFirstTime = beforeEuro.substring(0, firstTime.first).trim()
                    val parts = beforeFirstTime.split(Pattern.compile("\\s+"))
                    if (parts.size >= 2) {
                        parts.drop(2).joinToString(" ").trim()
                            .replace(transportTypePattern.pattern().toRegex(), "").trim()
                    } else {
                        beforeFirstTime
                    }
                } else "Unknown"

                return departure to destination
            } else {
                // If no time found, try skipping the date and company
                val parts = beforeEuro.split(Pattern.compile("\\s+"))
                val stationPart = if (parts.size > 2) {
                    parts.drop(2).joinToString(" ")
                } else {
                    beforeEuro
                }
                return "Unknown" to stationPart.replace(transportTypePattern.pattern().toRegex(), "").trim()
            }
        }
        return "Unknown" to "Unknown"
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
            val lastCheckIn =
                pendingCheckIns.lastOrNull { it.type == segment.type && it.dateTime.toLocalDate() == segment.dateTime.toLocalDate() }
            if (lastCheckIn != null) {
                pendingCheckIns.remove(lastCheckIn)
                journeys.add(Journey(lastCheckIn, segment, segment.type))
            } else {
                // Check-out without a corresponding Check-in line in the PDF.
                // Create a dummy check-in
                val dummyCheckIn = Segment(
                    segment.dateTime.minusMinutes(30),
                    "Unknown",
                    segment.type,
                    CheckInOut.CHECKIN,
                    BigDecimal.ZERO
                )
                journeys.add(Journey(dummyCheckIn, segment, segment.type))
            }
        }
    }
    return journeys.sortedBy { it.checkIn.dateTime }

}
