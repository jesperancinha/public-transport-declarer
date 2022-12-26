package org.jesperancinha.ptd.domain

import arrow.core.firstOrNone
import org.jesperancinha.ptd.parsers.OVPublicTransporParser
import java.io.InputStream
import java.math.BigDecimal
import java.time.LocalDate
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

data class SegmentNode(
    val date: LocalDate? = null,
    val name: String,
    val next: SegmentNode? = null
)

internal class CalculatorDao(
    val notIncluded: List<String> = listOf("Arnhem", "Velp", "Schiphol"),
    val dailyCostLimit: BigDecimal = BigDecimal.TEN,
    val travelRoutes: List<List<SegmentNode>> = emptyList()
) {

    val ovPublicTransporParser: OVPublicTransporParser by lazy { OVPublicTransporParser() }

    /**
     * Only the destination cities are shown in the logs for the OVPublicTransportParser
     */
    fun dailyCosts(inputStream: InputStream) = run {
        val allSegments = ovPublicTransporParser.parseDocument(inputStream)

        allSegments.groupBy { it?.dateTime?.toLocalDate() }
            .map {
                println("${it.key} - ${it.value.map { segment -> segment?.station }.joinToString(" -> ")}")
                it.key to it.value.sumOf { segment ->
                    if (segment.notIncluded()) BigDecimal.ZERO else
                        segment?.cost ?: BigDecimal.ZERO
                }
            }
            .filter {
                it.second > dailyCostLimit
            }
            .onEach { println(it) }


    }

    private fun Segment?.notIncluded(): Boolean = notIncluded.firstOrNone { not ->
        this?.station?.contains(not) == true || this?.company?.contains(not) == true
    }.isNotEmpty()

}
