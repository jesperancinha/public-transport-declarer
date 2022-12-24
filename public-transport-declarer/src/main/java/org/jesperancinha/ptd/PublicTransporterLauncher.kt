package org.jesperancinha.ptd

import org.apache.tika.exception.TikaException
import org.jesperancinha.ptd.domain.CalculatorDao
import org.xml.sax.SAXException
import java.io.IOException

object PublicTransporterLauncher {
    @Throws(TikaException::class, IOException::class, SAXException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        CalculatorDao().dailyCosts(
            PublicTransporterLauncher::class.java.getResourceAsStream("/declaratieoverzicht_22122022110627.pdf")
        )
    }
}