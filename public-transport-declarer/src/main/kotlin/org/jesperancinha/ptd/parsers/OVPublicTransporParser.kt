package org.jesperancinha.ptd.parsers

import arrow.core.continuations.nullable
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.parser.PdfTextExtractor
import org.jesperancinha.ptd.domain.CheckInOut.CHECKIN
import org.jesperancinha.ptd.domain.CheckInOut.CHECKOUT
import org.jesperancinha.ptd.domain.Currency
import org.jesperancinha.ptd.domain.Segment
import java.math.BigDecimal
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

internal const val DATE_PATTERN = "dd-MM-yyyy"
internal const val DATE_PATTERN_2 = "yyyy-MM-dd"
private const val TIME_PATTERN = "HH:mm"
private val TIME_PATTERN_REGEX = Pattern.compile("[0-9]{2}:[0-9]{2}")
private val STRING_PATTERN_REGEX = Pattern.compile("([a-zA-Z, '.(]+(-)?[a-zA-Z, '.(]+)")
private val CHECKOUT_PATTERN_REGEX = Pattern.compile("(Check-uit)")
private val CURRENCY_PATTERN = Pattern.compile("(\$|€)( *[0-9]+,?[0-9]*)")
private val CURRENCY_TYPE_PATTERN = Pattern.compile("(\$|€)")
private const val SPACE_DELIMITER = " "
private const val TRIPLE_POINTS_DELIMITER = "..."

class PtdPdfReader {

    val error = AtomicBoolean(false)

    fun readStream(fileUrl: URL): String = PdfReader(fileUrl)
        .let { reader ->
            val pdfTextExtractor = PdfTextExtractor(reader)
            (0..reader.numberOfPages)
                .joinToString("\n") {
                    try {
                        pdfTextExtractor.getTextFromPage(it).trim()
                    } catch (ex: Exception) {
                        logger.error(ex)
                        error.set(true)
                        ""
                    }
                }
        }

    companion object {
        private val logger = object {
            fun error(text: Any) = println(text)
        }
    }
}

/**
 * Parses PDF files generated in the OV Chipkaart website https://www.ov-chipkaart.nl
 *
 * OV means "Openbaar Vervoer" and it is the way to designate everything related to transport via:
 * NS, Arriva, OV-Chipkaart
 * This comprises:
 * Buses, Trains and Trams in the whole country.
 *
 */
class OVPublicTransporParser : IPublicTransportParser {

    val pdfReader: PtdPdfReader by lazy { PtdPdfReader() }
    val error = AtomicBoolean(false)

    override fun parseDocument(fileUrl: URL) = run {

        logger.info(">>>>> Raw Segments")
        pdfReader.readStream(fileUrl).split("\n").filter { isTransportLine(it) }.mapNotNull {
            logger.info(it)
            createDataObject(it)
        }
            .also { logger.info(">>>>> Parsed Segments") }
            .also {
                if (pdfReader.error.get()) {
                    logger.info(">>>>>>>>>>>>>>> WARNING -> An error has been detected while parsing the pdf file!")
                    logger.info(">>>>>>>>>>>>>>> In these cases it is suggested to run the application via the jar file.")
                    error.set(true)
                }
            }
            .onEach { segment -> logger.info(segment) }
    }

    fun createDataObject(segmentString: String) = nullable.eager {
        Segment(
            dateTime = parseDateTime(segmentString).bind(),
            company = parseCompany(segmentString).bind(),
            station = parseStation(segmentString).bind(),
            check = parseCheckout(segmentString).bind(),
            cost = parseCost(segmentString).bind(),
            currency = parseCurrency(segmentString).bind()

        )
    }

    private fun parseCurrency(segmentString: String) = CURRENCY_TYPE_PATTERN.matcher(segmentString).run {
        find()
        when (group(1).replace(",", ".").trim()) {
            "€" -> Currency.EUR
            else -> null

        }
    }

    private fun parseCost(segmentString: String): BigDecimal = CURRENCY_PATTERN.matcher(segmentString).run {
        find()
        BigDecimal(group(2).replace(",", ".").trim())
    }

    private fun parseCheckout(segmentString: String) = CHECKOUT_PATTERN_REGEX.matcher(segmentString).run {
        if (find()) CHECKOUT else CHECKIN
    }

    private fun parseStation(segmentString: String): String = STRING_PATTERN_REGEX.matcher(segmentString).run {
        find()
        find()
        group(1).trim()
    }

    private fun parseCompany(segmentString: String): String = STRING_PATTERN_REGEX.matcher(segmentString).run {
        find()
        group(0).trim()
    }


    private fun parseDateTime(segmentString: String): LocalDateTime? = run {
        val dateString =
            segmentString.split(SPACE_DELIMITER, TRIPLE_POINTS_DELIMITER).firstOrNull { isDate(it) }
        val timeString = TIME_PATTERN_REGEX.matcher(segmentString).apply { find() }.group(0)
        if (dateString == null || timeString == null) {
            null
        } else
            LocalDateTime.parse(
                "$dateString $timeString",
                DateTimeFormatter.ofPattern("$DATE_PATTERN $TIME_PATTERN")
            )
    }


    private fun isDate(element: String) = try {
        LocalDate.parse(element, DateTimeFormatter.ofPattern(DATE_PATTERN))
        true
    } catch (_: Exception) {
        false
    }


    private fun isTime(element: String) = TIME_PATTERN_REGEX.matcher(element).run { find() }

    override fun isTransportLine(line: String) = try {
        val splitStringOnSpace = line.trim().split(SPACE_DELIMITER)
        LocalDate.parse(splitStringOnSpace[0], DateTimeFormatter.ofPattern(DATE_PATTERN))
        splitStringOnSpace.size > 1
    } catch (e: Exception) {
        false
    }

    companion object {
        private val logger = object {
            fun info(text: Any) = println(text)
        }
    }
}