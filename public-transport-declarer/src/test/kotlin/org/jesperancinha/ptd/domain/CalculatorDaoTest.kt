package org.jesperancinha.ptd.domain

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.ptd.domain.CheckInOut.CHECKOUT
import org.jesperancinha.ptd.domain.Currency.EUR
import org.jesperancinha.ptd.parsers.DATE_PATTERN
import org.jesperancinha.ptd.parsers.DATE_PATTERN_2
import org.jesperancinha.ptd.parsers.TIME_PATTERN
import org.jesperancinha.ptd.toSegmentNodeList
import org.junit.jupiter.api.Test
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("${DATE_PATTERN_2} $TIME_PATTERN")
private fun String.toLocalDateTime() = LocalDateTime.parse(this, dateTimeFormatter)

val testCases = listOf(
    Segment(
        dateTime = "2022-12-01 08:17".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-01 08:48".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-01 17:30".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-01 18:00".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-02 19:38".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-02 20:32".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Arnhem Centraal",
        check = CHECKOUT,
        cost = BigDecimal("11.90"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-05 08:27".toLocalDateTime(),
        company = "NS Arnhem Centraal",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("11.90"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-05 08:59".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-06 08:07".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, St. Antonius Zieke...",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-06 08:40".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-06 17:02".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-06 17:29".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-07 08:33".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-07 09:02".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-07 17:24".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-07 17:51".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, Nieuwegein City",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-08 08:08".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-08 08:48".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Amsterdam Amstel",
        check = CHECKOUT,
        cost = BigDecimal("7.30"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-08 17:59".toLocalDateTime(),
        company = "NS Amsterdam Amstel",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.30"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-08 18:30".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-09 08:31".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-09 09:04".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-09 17:16".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-09 18:00".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Arnhem Centraal",
        check = CHECKOUT,
        cost = BigDecimal("10.50"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-12 08:09".toLocalDateTime(),
        company = "NS Arnhem Centraal",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("11.90"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-12 08:38".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("5.70"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-12 17:17".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-12 17:44".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-13 07:49".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-13 08:22".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-13 17:13".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-13 17:41".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, Nieuwegein City",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-14 08:25".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-14 09:04".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-14 17:45".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-14 18:14".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-15 08:17".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-15 09:11".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-15 22:07".toLocalDateTime(),
        company = "NS Bodegraven",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("6.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-15 22:47".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 08:10".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, Nieuwegein City",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.64"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 08:51".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 08:57".toLocalDateTime(),
        company = "NS Gouda",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("0.00"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 17:37".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 17:41".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("1.01"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-16 18:21".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeurszijde (Per...",
        station = "Nieuwegein, Nieuwegein City",
        check = CHECKOUT,
        cost = BigDecimal("2.01"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-17 09:08".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, St. Antonius Zieke...",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("1.01"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-19 08:24".toLocalDateTime(),
        company = "NS Arnhem Centraal",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("11.90"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-19 09:03".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Gouda",
        check = CHECKOUT,
        cost = BigDecimal("5.70"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-19 17:44".toLocalDateTime(),
        company = "NS Gouda",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("7.10"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-19 18:10".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Utrecht Centraal",
        check = CHECKOUT,
        cost = BigDecimal("0.00"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-19 19:16".toLocalDateTime(),
        company = "Qbuzz Utrecht, CS Jaarbeursplein",
        station = "Nieuwegein, St. Antonius Zieke...",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-20 11:22".toLocalDateTime(),
        company = "Qbuzz Nieuwegein, St. Antonius Zieke...",
        station = "Utrecht, CS Jaarbeursplein",
        check = CHECKOUT,
        cost = BigDecimal("2.71"),
        currency = EUR,
        description = null
    ),
    Segment(
        dateTime = "2022-12-20 12:18".toLocalDateTime(),
        company = "NS Utrecht Centraal",
        station = "Schiphol Airport",
        check = CHECKOUT,
        cost = BigDecimal("9.70"),
        currency = EUR,
        description = null
    )
)

class CalculatorDaoTest {
    val calculatorDao by lazy {
        CalculatorDao(
            notIncluded = emptyList(),
            dailyCostLimit = BigDecimal.ZERO,
            travelRoutes = CalculatorDaoTest::class.java.getResourceAsStream("/routes.txt")
                ?.let { InputStreamReader(it).readLines().toSegmentNodeList() }
                ?: throw RuntimeException("Unable to read file routes.txt"))
    }

    @Test
    fun `should filter elements correctly`() {
        val filteredSegments = calculatorDao.filterAllSegments(testCases).onEach { println(it) }.toSet()

        filteredSegments.shouldNotBeNull()
        filteredSegments.shouldNotBeEmpty()
        filteredSegments.shouldHaveSize(44)
    }
}