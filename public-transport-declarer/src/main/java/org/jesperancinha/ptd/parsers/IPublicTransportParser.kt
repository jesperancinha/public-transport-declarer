package org.jesperancinha.ptd.parsers

import java.io.InputStream

interface IPublicTransportParser {

    fun parseDocument(inputStream: InputStream)

    fun isTransportLine(line: String): Boolean
}