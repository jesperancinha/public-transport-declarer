package org.jesperancinha.ptd.daily

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigDecimal
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
}
