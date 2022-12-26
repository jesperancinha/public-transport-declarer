package org.jesperancinha.ptd

import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.ptd.parsers.OVPublicTransporParserTest
import org.junit.jupiter.api.Test
import picocli.CommandLine

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
    }
}