package com.jesperancinha.ptd

import org.apache.tika.exception.TikaException
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import org.xml.sax.SAXException
import java.io.IOException

object PublicTransporterLauncher {
    @Throws(TikaException::class, IOException::class, SAXException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val handler = BodyContentHandler()
        val metadata = Metadata()
        val inputstream =
            PublicTransporterLauncher::class.java.getResourceAsStream("/declaratieoverzicht_22122022110627.pdf")
        val pcontext = ParseContext()
        val pdfparser = PDFParser()
        pdfparser.parse(inputstream, handler, metadata, pcontext)
        println("Contents of the PDF :$handler")
        println("Metadata of the PDF:")
        val metadataNames = metadata.names()
        for (name in metadataNames) {
            println(name + " : " + metadata[name])
        }
    }
}