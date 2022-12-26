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
    val dateTime: LocalDateTime,
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

data class DailyCost(
    val date: LocalDate,
    val description: String = "Event description here",
    val cost: BigDecimal
)

internal class CalculatorDao(
    val notIncluded: List<String> = listOf("Arnhem", "Velp", "Schiphol"),
    val dailyCostLimit: BigDecimal = BigDecimal.TEN,
    val travelRoutes: List<List<SegmentNode>> = emptyList()
) {

    val ovPublicTransporParser: OVPublicTransporParser by lazy { OVPublicTransporParser() }

    /**
     * Both source and destination stations are shown in the logs
     */
    fun dailyCosts(inputStream: InputStream) = run {
        val allSegments = ovPublicTransporParser.parseDocument(inputStream)

        val allPrefilteredSegments = allSegments
            .asSequence()
            .filterNotNull()
            .groupBy { it.dateTime.toLocalDate() }
            .map {
                println(it.allRoutesMessage())
                it.toSumOfAllCosts()
            }
            .filter {
                it.cost > dailyCostLimit
            }
            .onEach { println(it) }
            .toList()

        allPrefilteredSegments


    }

    private fun Map.Entry<LocalDate, List<Segment?>>.toSumOfAllCosts() = DailyCost(
        date = this.key,
        cost = this.value.sumOf { segment ->
            if (segment.notIncluded()) BigDecimal.ZERO else
                segment?.cost ?: BigDecimal.ZERO
        }
    )

    private inline fun <reified K : LocalDate?, V : List<Segment?>> Map.Entry<K, V>.allRoutesMessage(): String =
        "${this.key} - ${this.value.joinToString(" -> ") { segment -> "${segment?.company} - ${segment?.station}" }}"


    private fun Segment?.notIncluded(): Boolean = notIncluded.firstOrNone { not ->
        this?.station?.contains(not) == true || this?.company?.contains(not) == true
    }.isNotEmpty()

}


