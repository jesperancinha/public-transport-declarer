package org.jesperancinha.ptd

import org.jesperancinha.ptd.domain.CalculatorDao
import org.jesperancinha.ptd.domain.DailyCost
import org.jesperancinha.ptd.domain.SegmentNode
import org.jesperancinha.ptd.parsers.DATE_PATTERN_2
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.OutputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Callable
import kotlin.system.exitProcess

const val INSTRUCTION_ROUTES = """
The route file can contain two types of formats:
    On considering only routes:
        station -> station -> station ...
        Example: Nieuwegein -> Gouda
    On considering the same for one specific day:
        local date -> station -> station ...
        Example: 2022/12/08 -> Nieuwegein -> Amstelveen
"""

@Command(
    name = "public transport declarer",
    mixinStandardHelpOptions = true,
    version = ["1.0.0-alpha"],
    description = ["Makes an accurate calculation of the public transport usage to make work related declarations"]
)
class PublicTransporterCommand : Callable<Int> {

    @Option(
        names = ["-o", "--origin"],
        description = ["The complete declaration file of your public tr ansportation provider"]
    )
    var origin: String? = null

    @Option(
        names = ["-d", "--destination"],
        description = ["The destination CSV file with the costs list per datetime/value"],
        defaultValue = "report.csv"

    )
    var destination: String = "report.csv"

    @Option(
        names = ["-l", "--list"],
        description = ["A list of all stations to ignore. Defaults to an empty list"],
        defaultValue = ""
    )
    var notIncluded: String = ""

    @Option(
        names = ["-g", "--grenslimit"],
        description = ["Grens comes from dutch and it means limit. Daily values under this will be ignored. Defaults to 10"]
    )
    var limit: BigDecimal = BigDecimal.TEN

    @Option(
        names = ["-r", "-routes"],
        description = ["This file contains an exclusive filter where only the listed routes are valid and considered for calculation: $INSTRUCTION_ROUTES"]
    )
    var routeFile: String? = null

    override fun call(): Int = run {
        val travelRoutes = readTravelRoutesFromFile(routeFile)
        logger.info(">>>>> Travel routes")
        logger.info(travelRoutes)
        val calculatorDao = CalculatorDao(
            notIncluded = if (notIncluded == "") emptyList() else notIncluded.split(",").toList(),
            dailyCostLimit = limit,
            travelRoutes = travelRoutes
        )
        val dailyCosts = calculatorDao.dailyCosts(origin?.let { File(it).toURI().toURL() }
            ?: throw RuntimeException("Origin file is mandatory! Please use -o to provide the origin file. Run with -help for more info on how to run this command"))
        logger.info(">>>>> CSV Format")
        FileOutputStream(destination).apply { writeCsv(dailyCosts) }
        logger.info(">>>>> Markup Format")
        dailyCosts.toMarkup()
        logger.info("Report successfully writen to $destination!")
        if(calculatorDao.error.get()){
            logger.info(">>>>>>>>>>>>>>> WARNING -> An error has been detected while parsing the pdf file!")
            logger.info(">>>>>>>>>>>>>>> In these cases it is suggested to run the application via the jar file.")
        }
        0
    }

    private fun readTravelRoutesFromFile(routeFile: String?): List<List<SegmentNode>> =
        routeFile?.let { effectiveRouteFile ->
            FileReader(File(effectiveRouteFile)).readLines().toSegmentNodeList()
        } ?: emptyList()

    private fun List<DailyCost>.toMarkup() {
        logger.info("| Date | Description | Cost |")
        logger.info("|---|---|---|")
        this.forEach {
            logger.info("| ${it.date} | ${it.description} | ${it.cost} |")
        }
    }

    companion object {
        private val logger = object {
            fun info(text: Any) = println(text)
        }
    }
}

internal fun List<String>.toSegmentNodeList(): List<List<SegmentNode>> = map {
    val segs = it.split(">").map { seg -> seg.trim() }
    val first = segs.first()
    val dateStamp = first.toDate()
    val description = segs.last().trim()
    if (dateStamp == null) {
        segs.map { name -> SegmentNode(name = name, description = description) }.take(2)
    } else {
        segs.takeLast(segs.size - 1)
            .map { name -> SegmentNode(name = name, date = dateStamp, description = description) }.take(2)
    }
}

fun OutputStream.writeCsv(costs: List<DailyCost>) {
    val writer = bufferedWriter()
    writer.write("""Date, Description, Cost""")
    writer.newLine()
    costs.forEach {
        writer.write("${it.date}, ${it.description}, ${it.cost}")
        writer.newLine()
    }
    writer.flush()
}

private fun String.toDate() = try {
    LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_PATTERN_2))
} catch (_: Exception) {
    null
}

fun main(args: Array<String>) {
    val exitCode: Int = if (args.isEmpty()) CommandLine(PublicTransporterCommand()).execute(*arrayOf("-h"))
    else CommandLine(PublicTransporterCommand()).execute(*args)
    exitProcess(exitCode)
}
