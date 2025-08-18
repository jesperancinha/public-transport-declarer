package org.jesperancinha.ptd

import org.openpdf.text.Document
import org.openpdf.text.pdf.PdfReader
import org.openpdf.text.pdf.parser.PdfTextExtractor
import java.io.File

//class Dummy

fun main() {
    val reader = PdfReader(File("declaratieoverzicht_22122022110627.pdf").toURI().toURL())
    println(reader.fileLength)
    println(reader.info)
    println(reader.isEncrypted)
    println(reader.catalog)
    println(reader.catalog.keys)
    println(reader.catalog.keys.last())
    println(String(reader.catalog.keys.last().bytes))
    Document(reader.getPageSizeWithRotation(1)).apply { open() }
    val message = (0..reader.numberOfPages).joinToString("\n") { PdfTextExtractor(reader).getTextFromPage(it).trim() }
    println(message)
    reader.close()
}