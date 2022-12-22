package com.jesperancinha.ptd.parsers

import com.jesperancinha.ptd.PublicTransporterLauncher
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
        println("Contents of the PDF :$handler")
        println("Metadata of the PDF:")
        val metadataNames = metadata.names()
        for (name in metadataNames) {
            println(name + " : " + metadata[name])
        }
    }

    override fun isTransportLine(line: String) = try {
        LocalDate.parse(line.split(" ")[0],  DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        true
    } catch (e: Exception) {
        false
    }
}