package org.jesperancinha.ptd.daily

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
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

    @Option(
        names = ["-wct", "--work-chart-title"],
        description = ["The title of the work chart"],
        required = false,
        defaultValue = "Werktijd in de OV"
    )
    var workChartTitle: String = "Werktijd in de OV"

    @Option(
        names = ["-rt", "--report-template"],
        description = ["The report template text file to use"],
        required = false,
        defaultValue = "report-template.txt"
    )
    var reportTemplateFile: String = "report-template.txt"

    @Option(
        names = ["-rtov", "--report-template-ov"],
        description = ["The report template text file to use for the OV chart"],
        required = false,
        defaultValue = "report-work-ov-template.txt"
    )
    var reportTemplateOvFile: String = "report-work-ov-template.txt"

    @Option(
        names = ["-d", "--days"],
        description = ["The number of days to use for the chart and the average work hours calculation"],
        required = false,
        defaultValue = "10"
    )
    var days: Int = 10

    private val parser = DailyPdfParser()
    private val validator = DailyPdfValidator()
    private val reporter = DailyReporter()

    private data class ReportRequest(
        val subfolder: File,
        val dailyJourneys: DailyJourney,
        val totalMatches: Boolean,
        val newPdfFile: File
    )

    override fun call(): Int {
        val folder = File(inputFolder)
        if (!folder.isDirectory) {
            println("Error: $inputFolder is not a directory.")
            return 1
        }

        val allJourneys = mutableListOf<DailyJourney>()
        val reportRequests = mutableListOf<ReportRequest>()
        val seenReports = mutableSetOf<Pair<LocalDate, BigDecimal>>()

        folder.listFiles { _, name -> name.lowercase().endsWith(".pdf") }?.forEach { pdfFile ->
            println("Processing ${pdfFile.name}...")
            try {

                val segments = parser.parse(pdfFile.toURI().toURL())
                val dailyJourneys = segments.toDailyJourneys()
                val uniqueDays = segments.map { it.dateTime.toLocalDate() }.distinct()
                if (uniqueDays.size > 1) {
                    println("WARNING: PDF ${pdfFile.name} contains data for multiple days: ${uniqueDays.joinToString(", ")}. Deleting original PDF and skipping report generation.")
                    pdfFile.delete()
                    return@forEach
                }
                if (uniqueDays.size == 1) {
                    val date = uniqueDays.first()
                    val totalCost = dailyJourneys.completeJourneys
                        .filter { it.isComplete }
                        .sumOf { it.checkIn.cost + (it.checkOut?.cost ?: BigDecimal.ZERO) }
                    val reportKey = date to totalCost
                    if (seenReports.contains(reportKey)) {
                        println("WARNING: PDF ${pdfFile.name} has the same date ($date) and total cost ($totalCost) as a previously processed report. Deleting original PDF and skipping report generation.")
                        pdfFile.delete()
                        return@forEach
                    }
                    seenReports.add(reportKey)
                }
                allJourneys.add(dailyJourneys)
                val completeJourneys = dailyJourneys.completeJourneys
                val incompleteSegments = dailyJourneys.missedCheckoutSegments
                println("Found ${segments.size} segments:")
                println("- ${completeJourneys.size} journeys.")
                println("- ${completeJourneys.count { it.isComplete }} complete.")
                println("- ${incompleteSegments.count()} missing checkouts.")
                val totalMatches = validator.validate(pdfFile.toURI().toURL(), completeJourneys)

                if (segments.isNotEmpty()) {
                    val subfolderName = "${
                        segments[0].dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }-${pdfFile.nameWithoutExtension.replace("declaratieoverzicht_", "")}"
                    val subfolder = File(folder, subfolderName)
                    val newPdfFile = File(subfolder, pdfFile.name)
                    pdfFile.copyTo(newPdfFile, overwrite = true)

                    reportRequests.add(ReportRequest(subfolder, dailyJourneys, totalMatches, newPdfFile))
                }
            } catch (e: Exception) {
                println("Error processing ${pdfFile.name}: ${e.message}")
                e.printStackTrace()
            }
        } ?: println("No PDF files found in $inputFolder")

        if (allJourneys.isNotEmpty()) {
            val workTimeData = allJourneys.flatMap { it.completeJourneys }
                .filter { it.isComplete }
                .groupBy { it.checkIn.dateTime.toLocalDate() }
                .mapValues { (_, journeys) ->
                    java.time.Duration.ofMinutes(
                        journeys.fold(java.time.Duration.ZERO) { acc, journey -> acc.plus(journey.duration) }.toMinutes()
                    ).toMinutes() / 60.0
                }
                .toSortedMap()

            reportRequests.forEach { request ->
                reporter.generateReport(
                    request.subfolder,
                    request.dailyJourneys,
                    request.totalMatches,
                    request.newPdfFile,
                    templatePdf?.let { File(it) },
                    headerFile?.let { File(it) },
                    workTimeData,
                    workChartTitle,
                    File("reports", reportTemplateFile),
                    File("reports", reportTemplateOvFile),
                    days
                )
                println("Finished processing ${request.newPdfFile.name}. Report generated in ${request.subfolder.absolutePath}")
            }

            reporter.generateExcelReport(folder, allJourneys, workChartTitle)
        }

        return 0
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(DailyPublicTransporterCommand()).execute(*args)
    exitProcess(exitCode)
}
