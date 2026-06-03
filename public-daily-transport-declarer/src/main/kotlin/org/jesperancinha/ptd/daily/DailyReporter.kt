package org.jesperancinha.ptd.daily

import org.openpdf.text.*
import org.openpdf.text.pdf.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xddf.usermodel.*
import org.apache.poi.xddf.usermodel.chart.*
import org.apache.poi.xssf.usermodel.XSSFChart
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DailyReporter {
    private val template = this::class.java.getResource("/report-template.txt")?.readText()
        ?: "Journey from {{checkInStation}} to {{checkOutStation}} by {{transportType}}.\nCheck-in: {{checkInTime}}\nCheck-out: {{checkOutTime}}\nCost: {{cost}}\n"

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    fun generateReport(
        folder: File,
        dailyJourney: DailyJourney,
        totalMatches: Boolean,
        originalPdfFile: File? = null,
        templatePdf: File? = null,
        headerFile: File? = null
    ) {
        if (!folder.exists()) folder.mkdirs()

        val headerContent = headerFile?.let {
            if (it.exists()) {
                it.readText()
            } else {
                println("WARNING: Header file ${it.absolutePath} not found. Using empty string.")
                ""
            }
        } ?: ""

        val reportFile = File(folder, "report.txt")
        val errorFile = File(folder, "error.txt")
        val logFile = File(folder, "log.txt")

        val errorContent = StringBuilder()
        val journeys = dailyJourney.completeJourneys
        val incompleteSegments = dailyJourney.missedCheckoutSegments

        val reportContentWithJourneys = template.createContent(markupLabelRegex = "---journeys([\\s\\S]*?)---") {
            journeys.filter { it.isComplete }.joinToString("\n") { journey ->
                val journeyCost = journey.checkIn.cost + (journey.checkOut?.cost ?: BigDecimal.ZERO)
                val duration = journey.duration.toMinutes()
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
                    .replace(
                        "{{duration}}", journey.duration.toDurationString()
                    ) + when {
                    duration == 0L -> " **"
                    duration < 5 -> " *"
                    else -> ""
                }
            }
        }.createContent("---incomplete-title([\\s\\S]*?)---") {
            if (incompleteSegments.isNotEmpty()) this else ""
        }.createContent("---incomplete([\\s\\S]*?)---") {
            incompleteSegments.joinToString("\n") { segment ->
                val journeyCost = segment.cost
                this
                    .replace("{{checkInStationInc}}", segment.station)
                    .replace("{{transportTypeInc}}", segment.type.nlName)
                    .replace("{{checkInTimeInc}}", segment.dateTime.format(dateTimeFormatter).split(" ").last())
                    .replace("{{costInc}}", String.format("%.2f", journeyCost))
            }
        }


        val totalCost =
            journeys.filter { it.isComplete }.sumOf { it.checkIn.cost + (it.checkOut?.cost ?: BigDecimal.ZERO) }
        val totalDuration = journeys.filter { it.isComplete }
            .fold(Duration.ZERO) { acc, journey -> acc.plus(journey.duration) }

        val fullReport = reportContentWithJourneys.replace("{{totalCost}}", String.format("%.2f", totalCost))
            .replace("{{totalDuration}}", totalDuration.toDurationString())
            .replace(
                "{{date}}",
                journeys.first().checkIn.dateTime.toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("nl", "NL")))
            )
            .replace("\n\n\n", "\n\n")
            .replace("\n\n\n", "\n\n")
            .replace("\n\n\n", "\n\n")

        val fullReportWithHeader = if (headerContent.isNotEmpty()) {
            "$headerContent\n\n$fullReport"
        } else {
            fullReport
        }

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

        reportFile.writeText(fullReportWithHeader)
        generatePDFReport(folder, fullReportWithHeader, templatePdf)
        originalPdfFile?.let {
            mergePDFReports(folder, it)
        }
        errorFile.writeText(errorContent.toString())

        val logContent = if (totalMatches) {
            "SUCCESS: Total cost matches the PDF total.\n"
        } else {
            "WARNING: Total cost DOES NOT match the PDF total.\n"
        }
        logFile.writeText(logContent + "Processed ${journeys.size} journeys (${journeys.filter { it.isComplete }.size} complete, ${journeys.filter { !it.isComplete }.size} incomplete).\n")
    }

    private fun generatePDFReport(folder: File, fullReport: String, templatePdf: File?) {
        val pdfFile = File(folder, "report.pdf")
        val document = Document(PageSize.A4)
        val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))
        val reader = templatePdf?.let {
            if (it.exists()) {
                PdfReader(it.absolutePath)
            } else {
                println("WARNING: Template file ${it.absolutePath} not found. Using blank sheet.")
                null
            }
        }
        val pageTemplate = reader?.let { writer.getImportedPage(it, 1) }

        writer.setPageEvent(object : PdfPageEventHelper() {
            override fun onEndPage(writer: PdfWriter, document: Document) {
                pageTemplate?.let {
                    writer.directContentUnder.addTemplate(it, 0f, 0f)
                }
            }
        })

        document.open()
        val font = Font(Font.HELVETICA, 12f)
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        val cell = PdfPCell()
        cell.border = Rectangle.NO_BORDER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.fixedHeight = document.pageSize.height - document.topMargin() - document.bottomMargin()

        fullReport.split("\n").forEach { line ->
            val p = Paragraph(line.ifEmpty { " " }, font)
            p.alignment = Element.ALIGN_JUSTIFIED
            cell.addElement(p)
        }
        table.addCell(cell)
        document.add(table)
        document.close()
        reader?.close()
    }

    private fun mergePDFReports(folder: File, originalPdfFile: File) {
        val pdfFile = File(folder, "report.pdf")
        val completeReportFile = File(folder, "${folder.nameWithoutExtension}-complete-report.pdf")
        val document = Document()
        val copy = PdfCopy(document, FileOutputStream(completeReportFile))
        document.open()
        val reader1 = PdfReader(FileInputStream(pdfFile))
        for (i in 1..reader1.numberOfPages) {
            copy.addPage(copy.getImportedPage(reader1, i))
        }
        val reader2 = PdfReader(FileInputStream(originalPdfFile))
        for (i in 1..reader2.numberOfPages) {
            copy.addPage(copy.getImportedPage(reader2, i))
        }
        document.close()
        reader1.close()
        reader2.close()
    }

    private fun String.createContent(
        markupLabelRegex: String,
        contentCreator: String.() -> String
    ): String {
        val journeysSectionRegex = Regex(markupLabelRegex)
        val matchResult = journeysSectionRegex.find(this)
        val reportContent = if (matchResult != null) {
            val journeyTemplate = matchResult.groupValues[1].trim()
            journeyTemplate.contentCreator()
        } else {
            "Template Error: $markupLabelRegex section not found"
        }
        return this.replace(journeysSectionRegex, reportContent)
    }

    fun generateExcelReport(folder: File, allJourneys: List<DailyJourney>, workChartTitle: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Work Time")
        val data = allJourneys.flatMap { it.completeJourneys }
            .filter { it.isComplete }
            .groupBy { it.checkIn.dateTime.toLocalDate() }
            .mapValues { (_, journeys) ->
                journeys.fold(Duration.ZERO) { acc, journey -> acc.plus(journey.duration) }.toMinutes() / 60.0
            }
            .toSortedMap()

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Date")
        headerRow.createCell(1).setCellValue("Work Time (Hours)")

        data.entries.forEachIndexed { index, entry ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(entry.key.toString())
            row.createCell(1).setCellValue(entry.value)
        }

        val drawing = sheet.createDrawingPatriarch()
        val anchor = drawing.createAnchor(0, 0, 0, 0, 3, 1, 15, 20)
        val chart = drawing.createChart(anchor) as XSSFChart
        chart.setTitleText(workChartTitle)
        chart.setTitleOverlay(false)

        val xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
        xAxis.setTitle("Day")
        val yAxis = chart.createValueAxis(AxisPosition.LEFT)
        yAxis.setTitle("Total Work Time (Hours)")

        val workTimeRange = CellRangeAddress(1, data.size, 1, 1)

        val categories = XDDFDataSourcesFactory.fromArray(data.keys.map { it.toString() }.toTypedArray())
        val values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, workTimeRange)

        val chartData = chart.createData(ChartTypes.BAR, xAxis, yAxis) as XDDFBarChartData
        chartData.barDirection = BarDirection.COL
        val series = chartData.addSeries(categories, values)
        series.setTitle("Work Time", null)

        val fill = XDDFSolidFillProperties(XDDFColor.from(PresetColor.CHARTREUSE))
        series.setFillProperties(fill)

        chart.plot(chartData)

        val minDate = data.firstKey().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val maxDate = data.lastKey().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val fileName = "$minDate-to-$maxDate-work-in-the-ov.xlsx"
        val file = File(folder, fileName)
        FileOutputStream(file).use {
            workbook.write(it)
        }
        workbook.close()
        println("Excel report generated: ${file.absolutePath}")
    }
}
