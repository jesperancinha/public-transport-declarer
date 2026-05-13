package org.jesperancinha.ptd.oncall

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class OnCallInterpreterLauncherTest {

    @Test
    fun `should process input folder and create monthly subfolders`() {
        val tempDir = Files.createTempDirectory("oncall-test").toFile()
        
        // Copy resources to temp dir
        val icsFile = File(javaClass.getResource("/oncallschedule_test.ics")?.path?: throw RuntimeException("Expected file not found!"))
        
        icsFile.copyTo(File(tempDir, "oncallschedule_test.ics"))
        
        val launcher = OnCallInterpreterCommand()
        launcher.inputFolder = tempDir.absolutePath
        
        val result = launcher.call()
        
        assertTrue(result == 0)
        
        // Check for subfolders. Based on oncallschedule_test.ics, there should be some months.
        // I'll check if subfolders follow the YYYY-MM pattern
        val subfolders = tempDir.listFiles { f -> f.isDirectory && f.name.matches(Regex("\\d{4}-\\d{2}-.*")) }
        assertTrue(subfolders != null && subfolders.isNotEmpty(), "No monthly subfolders with correct naming convention created")
        
        subfolders?.forEach { subfolder ->
            assertTrue(File(subfolder, "report.txt").exists(), "report.txt missing in ${subfolder.name}")
            assertTrue(File(subfolder, "log.txt").exists(), "log.txt missing in ${subfolder.name}")
            assertTrue(subfolder.listFiles { _, name -> name.endsWith(".xls") }?.isNotEmpty() == true, "XLS file missing in ${subfolder.name}")
        }
        
        tempDir.deleteRecursively()
    }
}
