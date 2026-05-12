package org.jesperancinha.ptd.oncall

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.time.Month

class OnCallInterpreterTest {

    val xltCalendarFile =
        File(javaClass.getResource("/calendar.xlt")?.path ?: throw RuntimeException("Expected file not found!"))
    val icsFile = File(
        javaClass.getResource("/oncallschedule_test.ics")?.path ?: throw RuntimeException("Expected file not found!")
    )

    @Test
    fun `should interpret ics and fill excel correctly for April 2026`() {
        val outputFile = File("target/oncall_april_2026.xls")
        val expectedFile = File(
            javaClass.getResource("/expected_oncall_april_2026.xls")?.path
                ?: throw RuntimeException("Expected file not found!")
        )
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltCalendarFile, 2026, Month.APRIL)
        interpreter.interpret(outputFile)

        assertTrue(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")

        verifyExcelFiles(outputFile, expectedFile)
    }

    private fun verifyExcelFiles(outputFile: File, expectedFile: File) {
        val workbookActual = HSSFWorkbook(FileInputStream(outputFile))
        val workbookExpected = HSSFWorkbook(FileInputStream(expectedFile))

        val sheetActual = workbookActual.getSheetAt(0)
        val sheetExpected = workbookExpected.getSheetAt(0)

        assertEquals(sheetExpected.lastRowNum, sheetActual.lastRowNum)

        for (i in 0..sheetActual.lastRowNum) {
            val rowActual = sheetActual.getRow(i)
            val rowExpected = sheetExpected.getRow(i)

            if (rowActual == null || rowExpected == null) {
                assertEquals(rowExpected, rowActual)
                continue
            }

            assertEquals(rowExpected.lastCellNum, rowActual.lastCellNum)
            for (j in 0 until rowActual.lastCellNum.toInt()) {
                val cellActual = rowActual.getCell(j)
                val cellExpected = rowExpected.getCell(j)

                if (cellActual == null || cellExpected == null) {
                    assertEquals(cellExpected, cellActual)
                    continue
                }

                assertEquals(cellExpected.cellType, cellActual.cellType)
                when (cellActual.cellType) {
                    org.apache.poi.ss.usermodel.CellType.NUMERIC -> assertEquals(
                        cellExpected.numericCellValue,
                        cellActual.numericCellValue
                    )

                    org.apache.poi.ss.usermodel.CellType.STRING -> assertEquals(
                        cellExpected.stringCellValue,
                        cellActual.stringCellValue
                    )

                    org.apache.poi.ss.usermodel.CellType.BOOLEAN -> assertEquals(
                        cellExpected.booleanCellValue,
                        cellActual.booleanCellValue
                    )

                    org.apache.poi.ss.usermodel.CellType.FORMULA -> assertEquals(
                        cellExpected.cellFormula,
                        cellActual.cellFormula
                    )

                    else -> {}
                }
            }
        }
    }

    @Test
    fun `should interpret ics and fill excel correctly for June 2026`() {
        val outputFile = File("target/oncall_june_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltCalendarFile, 2026, Month.JUNE)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }

    @Test
    fun `should interpret ics and fill excel correctly for October 2026`() {
        val outputFile = File("target/oncall_october_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltCalendarFile, 2026, Month.OCTOBER)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }

    @Test
    fun `should interpret ics and fill excel correctly for November 2026`() {
        val outputFile = File("target/oncall_november_2026.xls")
        outputFile.parentFile.mkdirs()

        val interpreter = OnCallInterpreter(icsFile, xltCalendarFile, 2026, Month.NOVEMBER)
        interpreter.interpret(outputFile)

        assert(outputFile.exists())
        println("Output generated at: ${outputFile.absolutePath}")
    }
}
