package com.jesperancinha.ptd

import com.jesperancinha.ptd.parsers.OVPublicTransporParser
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
        OVPublicTransporParser().parseDocument(
            PublicTransporterLauncher::class.java.getResourceAsStream("/declaratieoverzicht_22122022110627.pdf")
        )
    }
}