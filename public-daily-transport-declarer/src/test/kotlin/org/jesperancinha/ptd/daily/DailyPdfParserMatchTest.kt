package org.jesperancinha.ptd.daily

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class DailyPdfParserMatchTest {

    @Test
    fun `should match check-in and check-out segments in order`() {
        val now = LocalDateTime.now()
        val segments = listOf(
            Segment(now, "Station A", TransportType.TRAM, CheckInOut.CHECKIN, BigDecimal.ZERO),
            Segment(now.plusMinutes(10), "Station B", TransportType.TRAM, CheckInOut.CHECKOUT, BigDecimal("1.50")),
            Segment(now.plusMinutes(20), "Station B", TransportType.TRAM, CheckInOut.CHECKIN, BigDecimal.ZERO),
            Segment(now.plusMinutes(30), "Station C", TransportType.TRAM, CheckInOut.CHECKOUT, BigDecimal("2.00"))
        )
        
        val journeys = segments.toJourneys()
        
        journeys shouldHaveSize 2
        journeys[0].checkIn.station shouldBe "Station A"
        journeys[0].checkOut?.station shouldBe "Station B"
        journeys[1].checkIn.station shouldBe "Station B"
        journeys[1].checkOut?.station shouldBe "Station C"
    }

    @Test
    fun `should handle check-out without preceding check-in`() {
        val now = LocalDateTime.now()
        val segments = listOf(
            Segment(now, "Station B", TransportType.TRAM, CheckInOut.CHECKOUT, BigDecimal("1.50"))
        )
        
        val journeys = segments.toJourneys()
        
        journeys shouldHaveSize 1
        journeys[0].checkIn.station shouldBe "Unknown"
        journeys[0].checkOut?.station shouldBe "Station B"
    }

    @Test
    fun `should extract stations from a Check-in line`() {
        val parser = DailyPdfParser()
        val line = "01-12-2022 Qbuzz Nieuwegein, Nieuwegein City 08:17 Check-in"
        val segments = parser.parseLine(line)
        
        segments shouldHaveSize 1
        segments[0].check shouldBe CheckInOut.CHECKIN
        segments[0].station shouldBe "Nieuwegein, Nieuwegein City"
    }

    @Test
    fun `should extract stations from a Check-uit line`() {
        val parser = DailyPdfParser()
        val line = "01-12-2022 Qbuzz Nieuwegein, Nieuwegein City 08:17 Utrecht, CS Jaarbeursplein € 2,64 Check-uit"
        val segments = parser.parseLine(line)
        
        segments shouldHaveSize 1
        segments[0].check shouldBe CheckInOut.CHECKOUT
        segments[0].station shouldBe "Utrecht, CS Jaarbeursplein"
    }

    @Test
    fun `should match check-in and check-out segments in order across different transport types`() {
        val now = LocalDateTime.now()
        val segments = listOf(
            Segment(now, "Station A", TransportType.TRAM, CheckInOut.CHECKIN, BigDecimal.ZERO),
            Segment(now.plusMinutes(5), "Station X", TransportType.BUS, CheckInOut.CHECKIN, BigDecimal.ZERO),
            Segment(now.plusMinutes(10), "Station B", TransportType.TRAM, CheckInOut.CHECKOUT, BigDecimal("1.50")),
            Segment(now.plusMinutes(15), "Station Y", TransportType.BUS, CheckInOut.CHECKOUT, BigDecimal("2.00"))
        )
        
        val journeys = segments.toJourneys()
        
        journeys shouldHaveSize 2
        // They are sorted by check-in time
        journeys[0].checkIn.station shouldBe "Station A"
        journeys[0].type shouldBe TransportType.TRAM
        journeys[1].checkIn.station shouldBe "Station X"
        journeys[1].type shouldBe TransportType.BUS
    }

    private infix fun <T> List<T>.shouldHaveSize(size: Int) {
        this.size shouldBe size
    }
}
