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
    private val defaultTemplate = this::class.java.getResource("/report-template.txt")?.readText()
        ?: "Journey from {{checkInStation}} to {{checkOutStation}} by {{transportType}}.\nCheck-in: {{checkInTime}}\nCheck-out: {{checkOutTime}}\nCost: {{cost}}\n"

    private val defaultOvTemplate = this::class.java.getResource("/report-work-ov-template.txt")?.readText()
        ?: "Average work hours per day: {{workHours}}."

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    fun generateReport(
        folder: File,
        dailyJourney: DailyJourney,
        totalMatches: Boolean,
        originalPdfFile: File? = null,
        templatePdf: File? = null,
        headerFile: File? = null,
        workTimeData: Map<LocalDate, Double> = emptyMap(),
        workChartTitle: String = "Werktijd in de OV",
        reportTemplateFile: File? = null,
        reportTemplateOvFile: File? = null
    ) {
        if (!folder.exists()) folder.mkdirs()

        val template = reportTemplateFile?.let {
            if (it.exists()) it.readText() else {
                println("WARNING: Report template file ${it.absolutePath} not found. Using default template.")
                defaultTemplate
            }
        } ?: defaultTemplate

        val ovTemplate = reportTemplateOvFile?.let {
            if (it.exists()) it.readText() else {
                println("WARNING: OV Report template file ${it.absolutePath} not found. Using default OV template.")
                defaultOvTemplate
            }
        } ?: defaultOvTemplate

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

        val averageWorkHours = workTimeData.entries
            .sortedByDescending { it.key }
            .take(10)
            .map { it.value }
            .average()
            .let { if (it.isNaN()) 0.0 else it }

        val ovReportContent = ovTemplate.replace("{{workHours}}", String.format("%.2f", averageWorkHours))

        reportFile.writeText(fullReportWithHeader)
        generatePDFReport(
            folder,
            fullReportWithHeader,
            templatePdf,
            workTimeData,
            workChartTitle,
            headerContent,
            ovReportContent
        )
        generateOVReport(folder, templatePdf, workTimeData, workChartTitle, headerContent, ovReportContent)
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

    private fun generatePDFReport(
        folder: File,
        fullReport: String,
        templatePdf: File?,
        workTimeData: Map<LocalDate, Double>,
        workChartTitle: String,
        headerContent: String = "",
        ovReportContent: String = ""
    ) {
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
        val font = Font(Font.HELVETICA, 10f)

        // Page 1: Text report
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.setTotalWidth(document.pageSize.width - document.leftMargin() - document.rightMargin())
        val cell = PdfPCell()
        cell.border = Rectangle.NO_BORDER
        cell.verticalAlignment = Element.ALIGN_TOP
        cell.horizontalAlignment = Element.ALIGN_CENTER

        fullReport.split("\n").forEach { line ->
            val p = Paragraph(line.ifEmpty { " " }, font)
            p.alignment = Element.ALIGN_JUSTIFIED
            cell.addElement(p)
        }
        table.addCell(cell)

        val textHeight = table.getTotalHeight()
        val startY = (document.pageSize.height + textHeight) / 2 - 20f
        table.writeSelectedRows(0, -1, document.leftMargin(), startY, writer.directContent)

        // Page 2: Chart report
        if (workTimeData.isNotEmpty()) {
            document.newPage()
            if (headerContent.isNotEmpty()) {
                val pHeader = Paragraph(headerContent, font)
                pHeader.alignment = Element.ALIGN_JUSTIFIED
                document.add(pHeader)
                document.add(Paragraph(" "))
            }
            if (ovReportContent.isNotEmpty()) {
                val pOv = Paragraph(ovReportContent, font)
                pOv.alignment = Element.ALIGN_JUSTIFIED
                document.add(pOv)
                document.add(Paragraph(" "))
            }
            drawChart(writer, document, workTimeData, workChartTitle, (document.pageSize.height + 180f) / 2)
        }

        document.close()
        reader?.close()
    }

    private fun generateOVReport(
        folder: File,
        templatePdf: File?,
        workTimeData: Map<LocalDate, Double>,
        workChartTitle: String,
        headerContent: String = "",
        ovReportContent: String = ""
    ) {
        if (workTimeData.isEmpty()) return

        val pdfFile = File(folder, "report-ov.pdf")
        val document = Document(PageSize.A4)
        val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))
        val reader = templatePdf?.let {
            if (it.exists()) {
                PdfReader(it.absolutePath)
            } else {
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
        val font = Font(Font.HELVETICA, 10f)
        if (headerContent.isNotEmpty()) {
            val pHeader = Paragraph(headerContent, font)
            pHeader.alignment = Element.ALIGN_JUSTIFIED
            document.add(pHeader)
            document.add(Paragraph(" "))
        }
        if (ovReportContent.isNotEmpty()) {
            val pOv = Paragraph(ovReportContent, font)
            pOv.alignment = Element.ALIGN_JUSTIFIED
            document.add(pOv)
            document.add(Paragraph(" "))
        }
        drawChart(writer, document, workTimeData, workChartTitle, (document.pageSize.height + 180f) / 2)
        document.close()
        reader?.close()
    }

    private fun drawChart(
        writer: PdfWriter,
        document: Document,
        data: Map<LocalDate, Double>,
        title: String,
        startY: Float
    ) {
        val cb = writer.directContent
        val width = 500f
        val height = 180f
        val x = (document.pageSize.width - width) / 2
        val y = if (startY - height < document.bottomMargin()) document.bottomMargin() else startY - height

        // Draw background
        cb.setColorFill(java.awt.Color.WHITE)
        cb.rectangle(x, y, width, height)
        cb.fill()

        // Draw border
        cb.setColorStroke(java.awt.Color.BLACK)
        cb.setLineWidth(1f)
        cb.rectangle(x, y, width, height)
        cb.stroke()

        val margin = 30f
        val chartWidth = width - 2 * margin
        val chartHeight = height - 2 * margin
        val chartX = x + margin
        val chartY = y + margin

        // Draw Title
        val font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED)
        cb.beginText()
        cb.setFontAndSize(font, 10f)
        cb.showTextAligned(Element.ALIGN_CENTER, title, x + width / 2, y + height - 15f, 0f)
        cb.endText()

        if (data.isEmpty()) return

        val maxVal = data.values.maxOrNull() ?: 1.0
        val yAxisMax = Math.ceil(maxVal).let { if (it == 0.0) 1.0 else it }
        val barCount = data.size
        val barWidth = (chartWidth / barCount) * 0.8f
        val barGap = (chartWidth / barCount) * 0.2f

        // Draw Axes
        cb.setLineWidth(1f)
        cb.setColorStroke(java.awt.Color.BLACK)
        cb.moveTo(chartX, chartY)
        cb.lineTo(chartX + chartWidth, chartY)
        cb.moveTo(chartX, chartY)
        cb.lineTo(chartX, chartY + chartHeight)
        cb.stroke()

        // Y Axis Labels
        val labelFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED)
        cb.setFontAndSize(labelFont, 8f)
        for (i in 0..yAxisMax.toInt()) {
            val labelY = chartY + (i / yAxisMax.toFloat()) * chartHeight
            cb.moveTo(chartX - 3f, labelY)
            cb.lineTo(chartX, labelY)
            cb.stroke()
            cb.beginText()
            cb.showTextAligned(Element.ALIGN_RIGHT, i.toString(), chartX - 5f, labelY - 3f, 0f)
            cb.endText()
        }

        // Bars and X Axis Labels
        data.entries.forEachIndexed { index, entry ->
            val barX = chartX + index * (barWidth + barGap) + barGap / 2
            val barH = (entry.value / yAxisMax.toFloat()) * chartHeight

            // Bar
            cb.setColorFill(java.awt.Color(127, 255, 0)) // CHARTREUSE
            cb.rectangle(barX, chartY, barWidth, barH.toFloat())
            cb.fill()
            cb.setColorStroke(java.awt.Color.BLACK)
            cb.rectangle(barX, chartY, barWidth, barH.toFloat())
            cb.stroke()

            // X Label (Date)
            cb.beginText()
            cb.setColorFill(java.awt.Color.BLACK)
            val dateStr = entry.key.toString()
            cb.showTextAligned(Element.ALIGN_RIGHT, dateStr, barX + barWidth / 2, chartY - 5f, 45f)
            cb.endText()
        }
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
        try {
            xAxis.getOrAddTextProperties()
            val ctCatAxField = xAxis::class.java.getDeclaredField("ctCatAx")
            ctCatAxField.isAccessible = true
            val ctCatAx = ctCatAxField.get(xAxis)
            val txPr = ctCatAx::class.java.getDeclaredMethod("getTxPr").invoke(ctCatAx) ?: ctCatAx::class.java.getDeclaredMethod("addNewTxPr").invoke(ctCatAx)
            val bodyPr = txPr::class.java.getDeclaredMethod("getBodyPr").invoke(txPr) ?: txPr::class.java.getDeclaredMethod("addNewBodyPr").invoke(txPr)
            
            bodyPr::class.java.getDeclaredMethod("setRot", Int::class.javaPrimitiveType).invoke(bodyPr, -2700000)
            
            val setVertMethod = try {
                bodyPr::class.java.getDeclaredMethod("setVert", Class.forName("org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType\$Enum"))
            } catch (e: Exception) {
                bodyPr::class.java.getDeclaredMethod("setVert", Class.forName("org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType"))
            }
            
            val horzEnum = Class.forName("org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType").getField("HORZ").get(null)
            setVertMethod.invoke(bodyPr, horzEnum)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val yAxis = chart.createValueAxis(AxisPosition.LEFT)
        yAxis.setTitle("Total Work Time (Hours)")

        val workTimeRange = CellRangeAddress(1, data.size, 1, 1)
        val dateRange = CellRangeAddress(1, data.size, 0, 0)

        val categories = XDDFDataSourcesFactory.fromStringCellRange(sheet, dateRange)
        val values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, workTimeRange)

        val chartData = chart.createData(ChartTypes.BAR, xAxis, yAxis) as XDDFBarChartData
        chartData.barDirection = BarDirection.COL
        val series = chartData.addSeries(categories, values)
        series.setTitle("Work Time", null)

        val whiteFill = XDDFSolidFillProperties(XDDFColor.from(PresetColor.WHITE))
        val blackLine = XDDFLineProperties()
        blackLine.setFillProperties(XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLACK)))

        try {
            val getOrAddShapePropertiesMethod = chart.javaClass.getMethod("getOrAddShapeProperties")
            val chartShapeProperties = getOrAddShapePropertiesMethod.invoke(chart)
            val setFillMethod = chartShapeProperties.javaClass.getMethod("setFillProperties", XDDFFillProperties::class.java)
            setFillMethod.invoke(chartShapeProperties, whiteFill)
            val setLineMethod = chartShapeProperties.javaClass.getMethod("setLineProperties", XDDFLineProperties::class.java)
            setLineMethod.invoke(chartShapeProperties, blackLine)
        } catch (e: Exception) {
        }

        try {
            val getPlotAreaMethod = chart.javaClass.getMethod("getOrAddPlotArea")
            val plotArea = getPlotAreaMethod.invoke(chart)
            val setFillMethod = plotArea.javaClass.getMethod("setFillProperties", XDDFFillProperties::class.java)
            setFillMethod.invoke(plotArea, whiteFill)
        } catch (e: Exception) {
        }

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
