package org.jesperancinha.ptd.parsers

import arrow.core.continuations.nullable
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import org.jesperancinha.ptd.domain.CheckInOut.*
import org.jesperancinha.ptd.domain.Currency
import org.jesperancinha.ptd.domain.Segment
import java.io.InputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

private const val DATE_PATTERN = "dd-MM-yyyy"
private const val TIME_PATTERN = "HH:mm"
private val TIME_PATTERN_REGEX = Pattern.compile("[0-9]{2}:[0-9]{2}")
private val STRING_PATTERN_REGEX = Pattern.compile("([a-zA-Z, ]+)")
private val CHECKOUT_PATTERN_REGEX = Pattern.compile("(Check-uit)")
private val CURRENCY_PATTERN = Pattern.compile("(\$|€)( *[0-9]+,?[0-9]*)")
private val CURRENCY_TYPE_PATTERN = Pattern.compile("(\$|€)")
private const val SPACE_DELIMITER = " "
private const val TRIPLE_POINTS_DELIMITER = "..."

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
    override fun parseDocument(inputStream: InputStream) = run {
        val handler = BodyContentHandler()
        val metadata = Metadata()
        val pcontext = ParseContext()
        val pdfparser = PDFParser()
        pdfparser.parse(inputStream, handler, metadata, pcontext)
        handler.toString().split("\n").filter { isTransportLine(it) }.map {
            createDataObject(it).also { segment -> println(segment)}
        }
    }

    private fun createDataObject(segmentString: String) = nullable.eager {
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
            segmentString.split(SPACE_DELIMITER, TRIPLE_POINTS_DELIMITER).firstOrNull { toDate(it) != null }
        val timeString =
            segmentString.split(SPACE_DELIMITER, TRIPLE_POINTS_DELIMITER).firstOrNull { toTime(it) != null }
        if (dateString == null || timeString == null) {
            null
        } else
            LocalDateTime.parse(
                "$dateString $timeString",
                DateTimeFormatter.ofPattern("$DATE_PATTERN $TIME_PATTERN")
            )
    }


    private fun toDate(element: String) = try {
        LocalDate.parse(element, DateTimeFormatter.ofPattern(DATE_PATTERN))
        element
    } catch (_: Exception) {
        null
    }


    private fun toTime(element: String) = TIME_PATTERN_REGEX.matcher(element).run { if (find()) element else null }

    override fun isTransportLine(line: String) = try {
        val splitStringOnSpace = line.split(SPACE_DELIMITER)
        LocalDate.parse(splitStringOnSpace[0], DateTimeFormatter.ofPattern(DATE_PATTERN))
        splitStringOnSpace.size > 1
    } catch (e: Exception) {
        false
    }
}