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
            Segment(
                now,
                "Station A",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKIN,
                cost = BigDecimal.ZERO
            ),
            Segment(
                now.plusMinutes(10),
                "Station B",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKOUT,
                cost = BigDecimal("1.50")
            ),
            Segment(
                now.plusMinutes(20),
                "Station B",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKIN,
                cost = BigDecimal.ZERO
            ),
            Segment(
                now.plusMinutes(30),
                "Station C",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKOUT,
                cost = BigDecimal("2.00")
            )
        )

        val journeys = segments.toDailyJourneys()

        val completeJourneys = journeys.completeJourneys
        completeJourneys shouldHaveSize 2
        completeJourneys[0].checkIn.station shouldBe "Station A"
        completeJourneys[0].checkOut?.station shouldBe "Station B"
        completeJourneys[1].checkIn.station shouldBe "Station B"
        completeJourneys[1].checkOut?.station shouldBe "Station C"
    }

    @Test
    fun `should handle check-out without preceding check-in`() {
        val now = LocalDateTime.now()
        val segments = listOf(
            Segment(
                now,
                "Station B",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKOUT,
                cost = BigDecimal("1.50")
            )
        )

        val journeys = segments.toDailyJourneys()

        val completeJourneys = journeys.completeJourneys
        completeJourneys shouldHaveSize 1
        completeJourneys[0].checkIn.station shouldBe UNKNOWN
        completeJourneys[0].checkOut?.station shouldBe "Station B"
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
            Segment(
                now,
                "Station A",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKIN,
                cost = BigDecimal.ZERO
            ),
            Segment(
                now.plusMinutes(5),
                "Station X",
                type = TransportType.BUS,
                check = CheckInOut.CHECKIN,
                cost = BigDecimal.ZERO
            ),
            Segment(
                now.plusMinutes(10),
                "Station B",
                type = TransportType.TRAM_BUS,
                check = CheckInOut.CHECKOUT,
                cost = BigDecimal("1.50")
            ),
            Segment(
                now.plusMinutes(15),
                "Station Y",
                type = TransportType.BUS,
                check = CheckInOut.CHECKOUT,
                cost = BigDecimal("2.00")
            )
        )

        val journeys = segments.toDailyJourneys()

        val completeJourneys = journeys.completeJourneys
        completeJourneys shouldHaveSize 2
        // They are sorted by check-in time
        completeJourneys[0].checkIn.station shouldBe "Station A"
        completeJourneys[0].type shouldBe TransportType.TRAM_BUS
        completeJourneys[1].checkIn.station shouldBe "Station X"
        completeJourneys[1].type shouldBe TransportType.BUS
    }

    private infix fun <T> List<T>.shouldHaveSize(size: Int) {
        this.size shouldBe size
    }
}
