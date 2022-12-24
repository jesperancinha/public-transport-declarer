package org.jesperancinha.ptd.domain

import arrow.core.firstOrNone
import org.jesperancinha.ptd.parsers.OVPublicTransporParser
import java.io.InputStream
import java.math.BigDecimal
import java.time.LocalDateTime

enum class CheckInOut {
    CHECKIN, CHECKOUT
}

enum class Currency {
    EUR,
    USD
}

data class Segment(
    val dateTime: LocalDateTime?,
    val company: String,
    val station: String,
    val check: CheckInOut,
    val cost: BigDecimal,
    val currency: Currency
)

internal class CalculatorDao(
    val notIncluded: List<String> = listOf("Arnhem", "Velp", "Schiphol"),
    val dailyCostLimit: BigDecimal = BigDecimal.TEN
) {

    val ovPublicTransporParser: OVPublicTransporParser by lazy { OVPublicTransporParser() }

    /**
     * Only the destination cities are shown in the logs for the OVPublicTransportParser
     */
    fun dailyCosts(inputStream: InputStream) {
        val allSegments = ovPublicTransporParser.parseDocument(inputStream)

        allSegments.groupBy { it?.dateTime?.toLocalDate() }
            .map {
                println("${it.key} - ${it.value.map { segment -> segment?.station }.joinToString(" -> ")}")
                it.key to it.value.sumOf { segment ->
                    if (notIncluded.firstOrNone { not ->
                            segment?.station?.contains(not) == true || segment?.company?.contains(not) == true
                        }
                            .isNotEmpty()) BigDecimal.ZERO else
                        segment?.cost ?: BigDecimal.ZERO
                }
            }
            .filter { it.second > dailyCostLimit }
            .forEach { println(it) }

    }
}
