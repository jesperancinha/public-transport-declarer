package org.jesperancinha.ptd.parsers

import org.jesperancinha.ptd.domain.Segment
import java.io.InputStream

interface IPublicTransportParser {

    fun parseDocument(inputStream: InputStream): List<Segment>

    fun isTransportLine(line: String): Boolean
}