package org.jesperancinha.ptd.parsers

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OVPublicTransporParserTest {

    @Test
    fun `should run control test`() {
        LocalDate.parse("09-12-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
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
}
