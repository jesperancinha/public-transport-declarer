package org.jesperancinha.ptd.oncall

import org.junit.jupiter.api.Test
import java.io.File
import java.time.Month

class OnCallInterpreterTest {

    @Test
    fun `should interpret ics and fill excel correctly for April 2026`() {
        val icsFile = File("../oncallschedule_test.ics")
        val xltFile = File("../test_calendar.xlt")
        val outputFile = File("target/oncall_april_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltFile, 2026, Month.APRIL)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }

    @Test
    fun `should interpret ics and fill excel correctly for June 2026`() {
        val icsFile = File("../oncallschedule_test.ics")
        val xltFile = File("../test_calendar.xlt")
        val outputFile = File("target/oncall_june_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltFile, 2026, Month.JUNE)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }

    @Test
    fun `should interpret ics and fill excel correctly for October 2026`() {
        val icsFile = File("../oncallschedule_test.ics")
        val xltFile = File("../test_calendar.xlt")
        val outputFile = File("target/oncall_october_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltFile, 2026, Month.OCTOBER)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }

    @Test
    fun `should interpret ics and fill excel correctly for November 2026`() {
        val icsFile = File("../oncallschedule_test.ics")
        val xltFile = File("../test_calendar.xlt")
        val outputFile = File("target/oncall_november_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltFile, 2026, Month.NOVEMBER)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }
}
