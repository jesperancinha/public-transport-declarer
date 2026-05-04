package org.jesperancinha.ptd.daily

import org.openpdf.text.pdf.PdfReader
import org.openpdf.text.pdf.parser.PdfTextExtractor
import java.math.BigDecimal
import java.net.URL
import java.util.regex.Pattern

class DailyPdfValidator {
    private val totalPattern = Pattern.compile("Totaal[ a-zA-Z]*€( *[0-9]+,?[0-9]*)", Pattern.CASE_INSENSITIVE)

    fun validate(fileUrl: URL, journeys: List<Journey>): Boolean {
        val expectedTotal = extractTotal(fileUrl)
        val actualTotal = journeys.filter { it.isComplete }.sumOf { 
            (it.checkIn.cost + (it.checkOut?.cost ?: BigDecimal.ZERO))
        }
        return expectedTotal?.let { it.compareTo(actualTotal) == 0 } ?: false
    }

    private fun extractTotal(fileUrl: URL): BigDecimal? {
        val reader = PdfReader(fileUrl)
        val extractor = PdfTextExtractor(reader)
        val text = (1..reader.numberOfPages).joinToString("\n") { extractor.getTextFromPage(it) }
        
        val matcher = totalPattern.matcher(text)
        return if (matcher.find()) {
            BigDecimal(matcher.group(1).replace(",", ".").trim())
        } else {
            null
        }
    }
}
