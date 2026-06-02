package org.jesperancinha.ptd.daily

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "public-daily-transport-declarer",
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Processes PDF transport files in a folder and generates reports per file."]
)
class DailyPublicTransporterCommand : Callable<Int> {

    @Option(
        names = ["-i", "--input"],
        description = ["The input folder containing PDF files"],
        required = true
    )
    lateinit var inputFolder: String

    @Option(
        names = ["-h", "--header"],
        description = ["The header text file to use for the report"],
        required = false,
        defaultValue = "reports/header.txt"
    )
    var headerFile: String? = null

    @Option(
        names = ["-t", "--template", "-template"],
        description = ["The template PDF file to use for the report"],
        required = false,
        defaultValue = "reports/template.pdf"
    )
    var templatePdf: String? = null

    private val parser = DailyPdfParser()
    private val validator = DailyPdfValidator()
    private val reporter = DailyReporter()

    override fun call(): Int {
        val folder = File(inputFolder)
        if (!folder.isDirectory) {
            println("Error: $inputFolder is not a directory.")
            return 1
        }

        folder.listFiles { _, name -> name.lowercase().endsWith(".pdf") }?.forEach { pdfFile ->
            println("Processing ${pdfFile.name}...")
            try {

                val segments = parser.parse(pdfFile.toURI().toURL())
                val dailyJourneys = segments.toDailyJourneys()
                val completeJourneys = dailyJourneys.completeJourneys
                val incompleteSegments = dailyJourneys.missedCheckoutSegments
                println("Found ${segments.size} segments:")
                println("- ${completeJourneys.size} journeys.")
                println("- ${completeJourneys.count { it.isComplete }} complete.")
                println("- ${incompleteSegments.count()} missing checkouts.")
                val totalMatches = validator.validate(pdfFile.toURI().toURL(), completeJourneys)

                if(segments.isNotEmpty()) {
                    val subfolderName = "${
                        segments[0].dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }-${pdfFile.nameWithoutExtension.replace("declaratieoverzicht_", "")}"
                    val subfolder = File(folder, subfolderName)
                    val newPdfFile = File(subfolder, pdfFile.name)
                    pdfFile.copyTo(newPdfFile, overwrite = true)

                    reporter.generateReport(
                        subfolder,
                        dailyJourneys,
                        totalMatches,
                        newPdfFile,
                        templatePdf?.let { File(it) },
                        headerFile?.let { File(it) })
                    println("Finished processing ${pdfFile.name}. Report generated in ${subfolder.absolutePath}")
                }
            } catch (e: Exception) {
                println("Error processing ${pdfFile.name}: ${e.message}")
                e.printStackTrace()
            }
        } ?: println("No PDF files found in $inputFolder")

        return 0
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(DailyPublicTransporterCommand()).execute(*args)
    exitProcess(exitCode)
}
