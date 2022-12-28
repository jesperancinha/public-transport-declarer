package org.jesperancinha.ptd.parsers

import org.jesperancinha.ptd.domain.Segment
import java.net.URL

interface IPublicTransportParser {

    fun parseDocument(fileUrl: URL): List<Segment>

    fun isTransportLine(line: String): Boolean
}