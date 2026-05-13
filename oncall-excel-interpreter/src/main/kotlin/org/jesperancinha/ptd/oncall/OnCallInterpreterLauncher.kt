package org.jesperancinha.ptd.oncall

import biweekly.Biweekly
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.text.Normalizer
import java.time.Month
import java.time.ZoneId
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "oncall-excel-interpreter",
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Processes ICS on-call files in a folder and generates Excel reports per month."]
)
class OnCallInterpreterCommand : Callable<Int> {

    @Option(
        names = ["-i", "--input"],
        description = ["The input folder containing ICS files"],
        required = true
    )
    lateinit var inputFolder: String

    private val reporter = OnCallReporter()

    override fun call(): Int {
        val folder = File(inputFolder)
        if (!folder.isDirectory) {
            println("Error: $inputFolder is not a directory.")
            return 1
        }

        folder.listFiles { _, name -> name.lowercase().endsWith(".ics") }?.forEach { icsFile ->
            println("Processing ${icsFile.name}...")
            try {
                val interpreter = OnCallInterpreter(icsFile)
                val events = Biweekly.parse(icsFile).all().flatMap { it.getEvents() }

                // Group events by year and month
                val monthsToProcess = events.map { event ->
                    val start = event.dateStart.value.toInstant().atZone(ZoneId.of("UTC"))
                    start.year to start.month
                }.distinct()

                monthsToProcess.forEach { (year, month) ->
                    val monthValue = "%02d".format(month.value)
                    val subfolderName = "$year-$monthValue-${
                        (Normalizer.normalize(
                            (icsFile.nameWithoutExtension
                                .replace(" ", "-")
                                .replace("(", "-")
                                .replace(")", "-")
                                .replace("--", "-")
                                .lowercase() + "_report")
                                .replace("-_", "-"), Normalizer.Form.NFD
                        )
                            .replace("\\p{M}+".toRegex(), ""))
                    }"
                    val subfolder = File(folder, subfolderName)
                    if (!subfolder.exists()) subfolder.mkdirs()

                    val outputFile = File(subfolder, "${icsFile.nameWithoutExtension}_$year-$monthValue.xls")

                    println("  Generating report for $month $year in ${subfolder.name}...")
                    interpreter.interpret(year, month, outputFile)

                    reporter.generateReport(
                        folder = subfolder,
                        success = true,
                        message = "Successfully processed ${icsFile.name} for $month $year"
                    )
                }

                println("Finished processing ${icsFile.name}.")
            } catch (e: Exception) {
                println("Error processing ${icsFile.name}: ${e.message}")
                val errorSubfolder = File(folder, "error-${icsFile.nameWithoutExtension}")
                reporter.generateReport(
                    folder = errorSubfolder,
                    success = false,
                    message = "Error processing ${icsFile.name}: ${e.message}",
                    error = e.stackTraceToString()
                )
            }
        } ?: println("No ICS files found in $inputFolder")

        return 0
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(OnCallInterpreterCommand()).execute(*args)
    exitProcess(exitCode)
}
