package org.jesperancinha.ptd.parsers

import arrow.core.continuations.nullable
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import org.jesperancinha.ptd.domain.CheckInOut
import org.jesperancinha.ptd.domain.Segment
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val DATE_PATTERN = "dd-MM-yyyy"

private const val TIME_PATTERN = "hh:mm"

private val STRING_PATTERN = Pattern.compile("(^[0-9][a-zA-Z, ]*^[0-9])")


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
    override fun parseDocument(inputStream: InputStream) {
        val handler = BodyContentHandler()
        val metadata = Metadata()
        val pcontext = ParseContext()
        val pdfparser = PDFParser()
        pdfparser.parse(inputStream, handler, metadata, pcontext)
        handler.toString().split("\n").filter { isTransportLine(it) }.forEach {
            println(it)
            createDataObject(it)
        }
//        println("Contents of the PDF :$handler")
//        println("Metadata of the PDF:")
//        val metadataNames = metadata.names()
//        for (name in metadataNames) {
//                println(name + " : " + metadata[name])
//        }
    }

    private fun createDataObject(segmentString: String) = nullable.eager {
        Segment(
            dateTime = parseDateTime(segmentString).bind(),
            company = parseCompany(segmentString).bind(),
            station = "",
            check = CheckInOut.CHECKOUT

        )
    }

    private fun parseCompany(segmentString: String): String = STRING_PATTERN.matcher(segmentString).run {
        find()
        group(1)
    }


    private fun parseDateTime(segmentString: String): LocalDateTime {
        val dateString = segmentString.split(" ").filter { toDate(it) != null }
        val timeString = segmentString.split(" ").filter { toTime(it) != null }
        return LocalDateTime.parse(
            "$dateString $timeString",
            DateTimeFormatter.ofPattern("$DATE_PATTERN $TIME_PATTERN")
        )
    }


    private fun toDate(element: String) =
        nullable.eager {
            try {
                LocalDate.parse(element, DateTimeFormatter.ofPattern(DATE_PATTERN))
            } catch (_: Exception) {
            }
        }

    private fun toTime(element: String) =
        nullable.eager {
            try {
                LocalTime.parse(element, DateTimeFormatter.ofPattern(TIME_PATTERN))
            } catch (_: Exception) {
            }
        }

    override fun isTransportLine(line: String) = try {
        val splitStringOnSpace = line.split(" ")
        LocalDate.parse(splitStringOnSpace[0], DateTimeFormatter.ofPattern(DATE_PATTERN))
        splitStringOnSpace.size > 1
    } catch (e: Exception) {
        false
    }
}