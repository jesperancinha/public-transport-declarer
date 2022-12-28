package org.jesperancinha.ptd.parsers

import io.kotest.matchers.bigdecimal.shouldBeGreaterThan
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.ptd.domain.CalculatorDao
import org.jesperancinha.ptd.domain.CheckInOut.CHECKOUT
import org.jesperancinha.ptd.domain.Currency.EUR
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OVPublicTransporParserTest {

    @Test
    fun `should run control test`() {
        LocalDate.parse("09-12-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    @Test
    fun `should parse corner case city names s-Hertogenbosch`() {
        OVPublicTransporParser().createDataObject("18-11-2022 NS Eindhoven Centraal 15:57 's-Hertogenbosch €  7,10 Check-uit Reizen op Saldo NS Vol tarief (...")
            .let { segment ->
                segment.shouldNotBeNull()
                segment.station shouldBe "'s-Hertogenbosch"
                segment.company shouldBe "NS Eindhoven Centraal"
                segment.dateTime shouldBe LocalDateTime.of(2022,11,18,15,57)
                segment.check shouldBe CHECKOUT
                segment.currency shouldBe EUR
                segment.cost shouldBe BigDecimal("7.10")
            }
    }

    @Test
    fun `should parse corner case shortened cities with triple points`() {
        OVPublicTransporParser().createDataObject("16-12-2022 Nieuwegein, Nieuwegein City (Per...18:21 Nieuwegein, Nieuwegein City €  2,01 Check-uit")
            .let { segment ->
                segment.shouldNotBeNull()
                segment.station shouldBe "Nieuwegein, Nieuwegein City"
                segment.company shouldBe "Nieuwegein, Nieuwegein City (Per..."
                segment.dateTime shouldBe LocalDateTime.of(2022,12,16,18,21)
                segment.check shouldBe CHECKOUT
                segment.currency shouldBe EUR
                segment.cost shouldBe BigDecimal("2.01")
            }
    }

    @Test
    fun `should detect dates correctly`() {
        val ovPublicTransporParser = OVPublicTransporParser()

        ovPublicTransporParser.isTransportLine("09-12-2022 NS Gouda 17:16 Utrecht Centraal €  7,10 Check-uit Reizen op Saldo NS Vol tarief (...")
            .shouldBeTrue()
        ovPublicTransporParser.isTransportLine("09-12-2022 NS Utrecht Centraal 18:00 Arnhem Centraal €  10,50 Check-uit Reizen op Saldo NS Vol tarief (...")
            .shouldBeTrue()
        ovPublicTransporParser.isTransportLine("12-12-2022 NS Arnhem Centraal 08:09 Utrecht Centraal €  11,90 Check-uit Reizen op Saldo NS Vol tarief (...")
            .shouldBeTrue()
        ovPublicTransporParser.isTransportLine("12-12-2022 NS Utrecht Centraal 08:38 Gouda €  5,70 Check-uit Reizen op Saldo NS Vol tarief (...")
            .shouldBeTrue()
        ovPublicTransporParser.isTransportLine("1-12-2022 NS Utrecht Centraal 08:38 Gouda €  5,70 Check-uit Reizen op Saldo NS Vol tarief (...")
            .shouldBeFalse()

    }

    @Test
    fun `should parse without errors for file declaratieoverzicht_22122022110627`() {
        val resourceAsStream =
            OVPublicTransporParserTest::class.java.getResource("/declaratieoverzicht_22122022110627.pdf")
        resourceAsStream.shouldNotBeNull()
        resourceAsStream.let {
            CalculatorDao().dailyCosts(it).forEach { costs -> costs.cost shouldBeGreaterThan BigDecimal.TEN }
        }
    }

    @Test
    fun `should parse without errors for file declaratieoverzicht_24122022170148`() {
        val resourceAsStream =
            OVPublicTransporParserTest::class.java.getResource("/declaratieoverzicht_24122022170148.pdf")
        resourceAsStream.shouldNotBeNull()
        resourceAsStream.let {
            CalculatorDao().dailyCosts(it).forEach { costs -> costs.cost shouldBeGreaterThan BigDecimal.TEN }
        }
    }
}
