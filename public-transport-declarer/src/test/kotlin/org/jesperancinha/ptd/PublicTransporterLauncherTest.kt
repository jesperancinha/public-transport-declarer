package org.jesperancinha.ptd

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.ptd.parsers.OVPublicTransporParserTest
import org.junit.jupiter.api.Test
import picocli.CommandLine
import java.io.File
import java.io.FileReader
import java.math.BigDecimal

class PublicTransporterLauncherTest {
    @Test
    fun `should use the route filter correctly`() {
        val resourceOrigin =
            OVPublicTransporParserTest::class.java.getResource("/declaratieoverzicht_22122022110627.pdf")
        val routes =
            OVPublicTransporParserTest::class.java.getResource("/routes.txt")
        resourceOrigin.shouldNotBeNull()
        routes.shouldNotBeNull()
        CommandLine(PublicTransporterCommand()).execute(*arrayOf("-g", "0", "-o", resourceOrigin.file, "-r", routes.file))

        val readLines = FileReader(File("report.csv")).readLines()
        readLines.shouldHaveSize(12)
        val map = readLines.map {
            it.split(",").map { str -> str.trim() }
        }
        map[1].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.55"
        }
        map[2].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.62"
        }
        map[3].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.48"
        }
        map[4].let { (date, description, value) ->
            description shouldBe "Kotlin Free Training Day"
            value shouldBe "19.95"
        }
        map[5].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "16.84"
        }
        map[6].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "15.51"
        }
        map[7].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.48"
        }
        map[8].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.55"
        }
        map[9].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "18.55"
        }
        map[10].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "19.86"
        }
        map[11].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "15.51"
        }


    }
}