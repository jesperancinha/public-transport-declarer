package org.jesperancinha.ptd.daily

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class DailyReporterTest {

    @Test
    fun `should generate PDF report`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        reporter.generateReport(tempFolder, dailyJourney, true)

        val reportTxt = File(tempFolder, "report.txt")
        val reportPdf = File(tempFolder, "report.pdf")

        reportTxt.exists() shouldBe true
        reportPdf.exists() shouldBe true
        (reportPdf.length() > 0) shouldBe true
    }

    @Test
    fun `should generate PDF report with template`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-template")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val templatePdf = File(tempFolder, "template.pdf")
        val doc = org.openpdf.text.Document()
        org.openpdf.text.pdf.PdfWriter.getInstance(doc, java.io.FileOutputStream(templatePdf))
        doc.open()
        doc.add(org.openpdf.text.Paragraph("Template Content"))
        doc.close()

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        reporter.generateReport(tempFolder, dailyJourney, true, templatePdf = templatePdf)

        val reportPdf = File(tempFolder, "report.pdf")
        reportPdf.exists() shouldBe true
        (reportPdf.length() > 0) shouldBe true
    }

    @Test
    fun `should generate PDF report with missing template`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-missing-template")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val templatePdf = File(tempFolder, "non-existent.pdf")

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        reporter.generateReport(tempFolder, dailyJourney, true, templatePdf = templatePdf)

        val reportPdf = File(tempFolder, "report.pdf")
        reportPdf.exists() shouldBe true
        (reportPdf.length() > 0) shouldBe true
    }

    @Test
    fun `should generate PDF report with merged original PDF`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-merged")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val originalPdf = File(tempFolder, "original.pdf")
        val document = org.openpdf.text.Document()
        org.openpdf.text.pdf.PdfWriter.getInstance(document, java.io.FileOutputStream(originalPdf))
        document.open()
        document.add(org.openpdf.text.Paragraph("Original PDF Content"))
        document.close()

        val now = java.time.LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        reporter.generateReport(tempFolder, dailyJourney, true, originalPdf)

        val reportPdf = File(tempFolder, "report.pdf")
        val completeReportPdf = File(tempFolder, "${tempFolder.nameWithoutExtension}-complete-report.pdf")

        reportPdf.exists() shouldBe true
        completeReportPdf.exists() shouldBe true
        (completeReportPdf.length() > reportPdf.length()) shouldBe true
    }

    @Test
    fun `should generate PDF report with header`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-header")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val headerFile = File(tempFolder, "header.txt")
        headerFile.writeText("Test Header Content")

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        reporter.generateReport(tempFolder, dailyJourney, true, headerFile = headerFile)

        val reportTxt = File(tempFolder, "report.txt")
        reportTxt.exists() shouldBe true
        reportTxt.readText().contains("Test Header Content") shouldBe true
        
        val reportPdf = File(tempFolder, "report.pdf")
        reportPdf.exists() shouldBe true
        (reportPdf.length() > 0) shouldBe true
    }

    @Test
    fun `should generate PDF report with OV template and average work hours`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-ov-template")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val ovTemplate = File(tempFolder, "ov-template.txt")
        ovTemplate.writeText("Average hours: {{workHours}}")

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(now, "A", type = TransportType.TRAIN, check = CheckInOut.CHECKIN, cost = BigDecimal.ZERO),
                    checkOut = Segment(now.plusHours(1), "B", type = TransportType.TRAIN, check = CheckInOut.CHECKOUT, cost = BigDecimal("2.50")),
                    type = TransportType.TRAIN
                )
            ),
            missedCheckoutSegments = emptyList()
        )

        val workTimeData = mapOf(
            LocalDate.now() to 8.0,
            LocalDate.now().minusDays(1) to 6.0
        )

        reporter.generateReport(
            tempFolder,
            dailyJourney,
            true,
            workTimeData = workTimeData,
            reportTemplateOvFile = ovTemplate
        )

        val reportPdf = File(tempFolder, "report-ov.pdf")
        reportPdf.exists() shouldBe true
        (reportPdf.length() > 0) shouldBe true
    }

    @Test
    fun `should reproduce Checkout-title bug when incomplete segments are present`() {
        val reporter = DailyReporter()
        val tempFolder = File("target/test-report-bug-repro")
        if (tempFolder.exists()) tempFolder.deleteRecursively()
        tempFolder.mkdirs()

        val now = LocalDateTime.now()
        val dailyJourney = DailyJourney(
            completeJourneys = listOf(
                Journey(
                    checkIn = Segment(
                        now,
                        "Station A",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKIN,
                        cost = BigDecimal.ZERO
                    ),
                    checkOut = Segment(
                        now.plusMinutes(10),
                        "Station B",
                        type = TransportType.TRAM_BUS,
                        check = CheckInOut.CHECKOUT,
                        cost = BigDecimal("1.50")
                    ),
                    type = TransportType.TRAM_BUS
                )
            ),
            missedCheckoutSegments = listOf(
                Segment(
                    now,
                    "Station C",
                    type = TransportType.TRAM_BUS,
                    check = CheckInOut.CHECKIN,
                    cost = BigDecimal("4.00")
                )
            )
        )

        reporter.generateReport(tempFolder, dailyJourney, true)

        val reportTxt = File(tempFolder, "report.txt")
        val content = reportTxt.readText()
        println("[DEBUG_LOG] Report content:\n$content")
        
        // If the bug exists, we might see something like "---incomplete-title Checkout-title Checkoutgemist:"
        // or just weird concatenation.
        content.contains("Checkout-title") shouldBe false
        content.contains("Checkoutgemist:") shouldBe false
        // We expect "Checkout gemist:" from the template, so "gemist:" will be present.
        // We only want to ensure it's not the corrupted version.
        content.contains("Checkout gemist:") shouldBe true
    }
}
