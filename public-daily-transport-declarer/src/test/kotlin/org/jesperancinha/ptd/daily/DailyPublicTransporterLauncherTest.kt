package org.jesperancinha.ptd.daily

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class DailyPublicTransporterLauncherTest {

    @Test
    fun `should process pdf files and create subfolders with reports`() {
        val resourcePdf = javaClass.getResource("/declaratieoverzicht_test.pdf")
        val tempDir = Files.createTempDirectory("ptd-test").toFile()
        val pdfFile = File(tempDir, "test.pdf")
        if (resourcePdf != null) {
            Files.copy(resourcePdf.openStream(), pdfFile.toPath())
        }

        val command = DailyPublicTransporterCommand()
        command.inputFolder = tempDir.absolutePath
        
        val result = command.call()
        
        result shouldBe 0
        val subfolder = File(tempDir, "test")
        subfolder.exists() shouldBe true
        subfolder.isDirectory shouldBe true
        
        File(subfolder, "report.txt").exists() shouldBe true
        File(subfolder, "error.txt").exists() shouldBe true
        File(subfolder, "log.txt").exists() shouldBe true
        
        tempDir.deleteRecursively()
    }
}
