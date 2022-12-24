package org.jesperancinha.ptd

import org.apache.tika.exception.TikaException
import org.jesperancinha.ptd.domain.CalculatorDao
import org.xml.sax.SAXException
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.IOException
import java.math.BigDecimal
import java.util.concurrent.Callable


@Command(
    name = "public transport declarer",
    mixinStandardHelpOptions = true,
    version = ["1.0.0-alpha"],
    description = ["Makes an accurate calculation of the public transport usage to make work related declarations"]
)
class PublicTransporterCommand : Callable<Int> {

    @Option(
        names = ["-o", "--origin"],
        description = ["The complete declaration file of your public transportation provider"],
        defaultValue = "declaratieoverzicht_22122022110627.pdf"
    )
    lateinit var origin: String

    @Option(
        names = ["-d", "--destination"],
        description = ["The destination CSV file with the costs list per datetime/value"],
        defaultValue = "report.csv"

    )
    lateinit var destination: String

    @Option(
        names = ["-l", "--list"],
        description = ["A list of all stations that we are supposed to ignore. Defaults to an empty list"],
        defaultValue = ""
    )
    lateinit var notIncluded: String

    @Option(
        names = ["-g", "-grenslimit"],
        description = ["Grens comes from dutch and it means limit. Daily values under this will be ignore. Defaults to 10"]
    )
    lateinit var limit: BigDecimal

    override fun call(): Int {
        TODO("Not yet implemented")
    }

}

object PublicTransporterLauncher {
    @Throws(TikaException::class, IOException::class, SAXException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        CalculatorDao().dailyCosts(
            PublicTransporterLauncher::class.java.getResourceAsStream("/declaratieoverzicht_22122022110627.pdf")
        )
    }
}