package org.jesperancinha.ptd

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.ptd.parsers.OVPublicTransporParserTest
import org.junit.jupiter.api.Test
import picocli.CommandLine
import java.io.File
import java.io.FileReader

class PublicTransporterLauncherTest {
    @Test
    fun `should use the route filter correctly for PDF files`() {
        val resourceOrigin =
            OVPublicTransporParserTest::class.java.getResource("/declaratieoverzicht_22122022110627.pdf")
        val routes =
            OVPublicTransporParserTest::class.java.getResource("/routes.txt")
        resourceOrigin.shouldNotBeNull()
        routes.shouldNotBeNull()
        CommandLine(PublicTransporterCommand()).execute(
            *arrayOf(
                "-g",
                "0",
                "-o",
                resourceOrigin.file,
                "-r",
                routes.file
            )
        )

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

    @Test
    fun `should use the route filter correctly for CSV files`() {
        val resourceOrigin =
            OVPublicTransporParserTest::class.java.getResource("/transacties_31032023194834.csv")
        val routes =
            OVPublicTransporParserTest::class.java.getResource("/routes.txt")
        resourceOrigin.shouldNotBeNull()
        routes.shouldNotBeNull()
        CommandLine(PublicTransporterCommand()).execute(
            *arrayOf(
                "-g",
                "0",
                "-o",
                resourceOrigin.file,
                "-r",
                routes.file,
                "-l",
                "Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche"
            )
        )

        val file = File("report.csv")
        val readLines = FileReader(file).readLines()
        file.deleteOnExit()
        readLines.shouldHaveSize(12)
        val map = readLines.map {
            it.split(",").map { str -> str.trim() }
        }
        map[1].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "15.00"
        }
        map[2].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "11.82"
        }
        map[3].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "17.82"
        }
        map[4].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "16.62"
        }
        map[5].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "20.72"
        }
        map[6].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "7.50"
        }
        map[7].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "11.60"
        }
        map[8].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "20.72"
        }
        map[9].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "10.14"
        }
        map[10].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "20.64"
        }
        map[11].let { (date, description, value) ->
            description shouldBe "Office work"
            value shouldBe "20.64"
        }

    }
}