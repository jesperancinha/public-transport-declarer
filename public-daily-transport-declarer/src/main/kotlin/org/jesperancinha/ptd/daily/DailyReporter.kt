package org.jesperancinha.ptd.daily

import java.io.File
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

class DailyReporter {
    private val template = this::class.java.getResource("/report-template.txt")?.readText()
        ?: "Journey from {{checkInStation}} to {{checkOutStation}} by {{transportType}}.\nCheck-in: {{checkInTime}}\nCheck-out: {{checkOutTime}}\nCost: {{cost}}\n"

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    fun generateReport(folder: File, journeys: List<Journey>, totalMatches: Boolean) {
        if (!folder.exists()) folder.mkdirs()

        val reportFile = File(folder, "report.txt")
        val errorFile = File(folder, "error.txt")
        val logFile = File(folder, "log.txt")

        val reportContent = StringBuilder()
        val errorContent = StringBuilder()

        journeys.forEach { journey ->
            if (journey.isComplete) {
                var journeyText = template
                    .replace("{{checkInStation}}", journey.checkIn.station)
                    .replace("{{checkOutStation}}", journey.checkOut?.station ?: "Unknown")
                    .replace("{{transportType}}", journey.type.name)
                    .replace("{{checkInTime}}", journey.checkIn.dateTime.format(dateTimeFormatter))
                    .replace("{{checkOutTime}}", journey.checkOut?.dateTime?.format(dateTimeFormatter) ?: "N/A")
                    .replace("{{cost}}", (journey.checkIn.cost + (journey.checkOut?.cost ?: BigDecimal.ZERO)).toString())
                reportContent.append(journeyText).append("\n---\n")
            } else {
                errorContent.append("Incomplete journey: Check-in at ${journey.checkIn.station} on ${journey.checkIn.dateTime.format(dateTimeFormatter)} by ${journey.type.name}\n")
            }
        }

        reportFile.writeText(reportContent.toString())
        errorFile.writeText(errorContent.toString())
        
        val logContent = if (totalMatches) {
            "SUCCESS: Total cost matches the PDF total.\n"
        } else {
            "WARNING: Total cost DOES NOT match the PDF total.\n"
        }
        logFile.writeText(logContent + "Processed ${journeys.size} journeys (${journeys.filter { it.isComplete }.size} complete, ${journeys.filter { !it.isComplete }.size} incomplete).\n")
    }
}
