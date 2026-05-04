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
                    checkIn = Segment(now, "Station A", TransportType.TRAM, CheckInOut.CHECKIN, BigDecimal.ZERO),
                    checkOut = Segment(now.plusMinutes(10), "Station B", TransportType.TRAM, CheckInOut.CHECKOUT, BigDecimal("1.50")),
                    type = TransportType.TRAM
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
}
