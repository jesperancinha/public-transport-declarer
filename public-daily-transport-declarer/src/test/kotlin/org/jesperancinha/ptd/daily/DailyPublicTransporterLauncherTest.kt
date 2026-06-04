package org.jesperancinha.ptd.daily

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class DailyPublicTransporterLauncherTest {

    @Test
    fun `should process pdf files and create subfolders with reports`() {
        val tempDir = Files.createTempDirectory("ptd-test").toFile()
        val pdfFile = File(tempDir, "test.pdf")
        
        val document = org.openpdf.text.Document()
        org.openpdf.text.pdf.PdfWriter.getInstance(document, java.io.FileOutputStream(pdfFile))
        document.open()
        document.add(org.openpdf.text.Paragraph("01-12-2022 Qbuzz Station A 08:17 Utrecht, CS Jaarbeursplein € 2,64 Check-uit"))
        document.close()

        val command = DailyPublicTransporterCommand()
        command.inputFolder = tempDir.absolutePath
        
        val result = command.call()
        
        result shouldBe 0
        val subfolder = File(tempDir, "2022-12-01-test")
        subfolder.exists() shouldBe true
        subfolder.isDirectory shouldBe true
        
        File(subfolder, "report.txt").exists() shouldBe true
        File(subfolder, "error.txt").exists() shouldBe true
        File(subfolder, "log.txt").exists() shouldBe true
        
        tempDir.deleteRecursively()
    }

    @Test
    fun `should delete original pdf and skip processing if multiple days are detected`() {
        // We need a real PDF that has multiple days if we want to test end-to-end,
        // but since parser is mocked by the actual file content, 
        // maybe we can create a PDF that parser will read as multiple days.
        // However, DailyPdfParser reads real PDFs.
        
        // Let's try to create a simple PDF with two lines that the parser might recognize as different days.
        // Actually, it's easier to use a mock or just rely on the logic if we could.
        // But this is an integration test.
        
        val tempDir = Files.createTempDirectory("ptd-test-multi").toFile()
        val pdfFile = File(tempDir, "multi-day.pdf")
        
        // Create a PDF that will be parsed as multiple days.
        // The parser looks for dates like dd-mm-yyyy.
        val document = org.openpdf.text.Document()
        org.openpdf.text.pdf.PdfWriter.getInstance(document, java.io.FileOutputStream(pdfFile))
        document.open()
        // We need to match what DailyPdfParser expects.
        // 01-12-2022 Qbuzz Nieuwegein, Nieuwegein City 08:17 Check-in
        document.add(org.openpdf.text.Paragraph("01-12-2022 Qbuzz Station A 08:17 Check-in"))
        document.add(org.openpdf.text.Paragraph("02-12-2022 Qbuzz Station B 08:17 Check-in"))
        document.close()
        
        val command = DailyPublicTransporterCommand()
        command.inputFolder = tempDir.absolutePath
        
        val result = command.call()
        
        result shouldBe 0
        pdfFile.exists() shouldBe false // Should be deleted
        
        // Check that no subfolders were created (other than the input folder itself)
        tempDir.listFiles { f -> f.isDirectory }?.size shouldBe 0
        
        tempDir.deleteRecursively()
    }
}
