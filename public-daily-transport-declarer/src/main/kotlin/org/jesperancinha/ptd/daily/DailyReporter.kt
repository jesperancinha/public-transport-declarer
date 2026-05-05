package org.jesperancinha.ptd.daily

import org.openpdf.text.Document
import org.openpdf.text.Font
import org.openpdf.text.Paragraph
import org.openpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.*

class DailyReporter {
    private val template = this::class.java.getResource("/report-template.txt")?.readText()
        ?: "Journey from {{checkInStation}} to {{checkOutStation}} by {{transportType}}.\nCheck-in: {{checkInTime}}\nCheck-out: {{checkOutTime}}\nCost: {{cost}}\n"

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    fun generateReport(folder: File, dailyJourney: DailyJourney, totalMatches: Boolean) {
        if (!folder.exists()) folder.mkdirs()

        val reportFile = File(folder, "report.txt")
        val errorFile = File(folder, "error.txt")
        val logFile = File(folder, "log.txt")

        val errorContent = StringBuilder()
        val journeys = dailyJourney.completeJourneys
        val incompleteSegments = dailyJourney.missedCheckoutSegments

        val reportContentWithJourneys = template.createContent(markupLabelRegex = "---journeys([\\s\\S]*?)---") {
            journeys.filter { it.isComplete }.joinToString("\n") { journey ->
                val journeyCost = journey.checkIn.cost + (journey.checkOut?.cost ?: BigDecimal.ZERO)
                this
                    .replace("{{checkInStation}}", journey.checkIn.station)
                    .replace("{{checkOutStation}}", journey.checkOut?.station ?: "Unknown")
                    .replace("{{transportType}}", journey.type.nlName)
                    .replace("{{checkInTime}}", journey.checkIn.dateTime.format(dateTimeFormatter).split(" ").last())
                    .replace(
                        "{{checkOutTime}}",
                        journey.checkOut?.dateTime?.format(dateTimeFormatter)?.split(" ")?.last() ?: "N/A"
                    )
                    .replace("{{cost}}", String.format("%.2f", journeyCost))
                    .replace("{{duration}}", journey.duration.toDurationString()) + if(journey.duration.toMinutes() < 5) " *" else ""
            }
        }.createContent( "---incomplete([\\s\\S]*?)---") {
                incompleteSegments.joinToString("\n") { segment ->
                    val journeyCost = segment.cost
                    this
                        .replace("{{checkInStationInc}}", segment.station)
                        .replace("{{transportTypeInc}}", segment.type.nlName)
                        .replace("{{checkInTimeInc}}", segment.dateTime.format(dateTimeFormatter).split(" ").last())
                        .replace("{{costInc}}", String.format("%.2f", journeyCost))
                }
            }
            .createContent("---incomplete-title([\\s\\S]*?)---"){
                if (incompleteSegments.isNotEmpty()) this else ""
            }

        val totalCost =
            journeys.filter { it.isComplete }.sumOf { it.checkIn.cost + (it.checkOut?.cost ?: BigDecimal.ZERO) }
        val totalDuration = journeys.filter { it.isComplete }
            .fold(Duration.ZERO) { acc, journey -> acc.plus(journey.duration) }

        val fullReport = reportContentWithJourneys.replace("{{totalCost}}", String.format("%.2f", totalCost))
            .replace("{{totalDuration}}", totalDuration.toDurationString())
            .replace("{{date}}", journeys.first().checkIn.dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("nl", "NL"))))
            .replace("\n\n\n", "\n\n")
            .replace("\n\n\n", "\n\n")
            .replace("\n\n\n", "\n\n")

        journeys.filter { !it.isComplete }.forEach { journey ->
            errorContent.append(
                "Incomplete journey: Check-in at ${journey.checkIn.station} on ${
                    journey.checkIn.dateTime.format(
                        dateTimeFormatter
                    )
                } by ${journey.type.name}\n"
            )
        }
        incompleteSegments.forEach { segment ->
            errorContent.append(
                "Incomplete segment: Check-in at ${segment.station} on ${
                    segment.dateTime.format(
                        dateTimeFormatter
                    )
                } by ${segment.type.name}\n"
            )
        }

        reportFile.writeText(fullReport)
        generatePDFReport(folder, fullReport)
        errorFile.writeText(errorContent.toString())

        val logContent = if (totalMatches) {
            "SUCCESS: Total cost matches the PDF total.\n"
        } else {
            "WARNING: Total cost DOES NOT match the PDF total.\n"
        }
        logFile.writeText(logContent + "Processed ${journeys.size} journeys (${journeys.filter { it.isComplete }.size} complete, ${journeys.filter { !it.isComplete }.size} incomplete).\n")
    }

    private fun generatePDFReport(folder: File, fullReport: String) {
        val pdfFile = File(folder, "report.pdf")
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(pdfFile))
        document.open()
        val font = Font(Font.HELVETICA, 12f)
        fullReport.split("\n").forEach { line ->
            document.add(Paragraph(line, font))
        }
        document.close()
    }

    private fun String.createContent(
        markupLabelRegex: String,
        contentCreator: String.() -> String
    ): String {
        val journeysSectionRegex = Regex(markupLabelRegex)
        val matchResult = journeysSectionRegex.find(template)
        val reportContent = if (matchResult != null) {
            val journeyTemplate = matchResult.groupValues[1].trim()
            journeyTemplate.contentCreator()
        } else {
            "Template Error: $this section not found"
        }
        return this.replace(journeysSectionRegex, reportContent)
    }
}
