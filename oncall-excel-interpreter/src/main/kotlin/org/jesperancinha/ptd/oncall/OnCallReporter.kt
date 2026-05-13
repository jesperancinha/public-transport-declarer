package org.jesperancinha.ptd.oncall

import java.io.File

class OnCallReporter {
    fun generateReport(folder: File, success: Boolean, message: String, error: String? = null) {
        if (!folder.exists()) folder.mkdirs()

        val reportFile = File(folder, "report.txt")
        val errorFile = File(folder, "error.txt")
        val logFile = File(folder, "log.txt")

        logFile.writeText("${if (success) "SUCCESS" else "FAILURE"}: $message\n")
        reportFile.writeText("On-Call Interpretation Report\nStatus: ${if (success) "Success" else "Failure"}\nMessage: $message\n")
        
        if (error != null) {
            errorFile.writeText(error)
        } else if (errorFile.exists()) {
            errorFile.delete()
        }
    }
}
