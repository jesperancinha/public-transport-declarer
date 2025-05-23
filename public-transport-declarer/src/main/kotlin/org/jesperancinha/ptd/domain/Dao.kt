package org.jesperancinha.ptd.domain

import arrow.core.firstOrNone
import org.jesperancinha.ptd.parsers.OVPublicTransporParser
import java.math.BigDecimal
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

enum class CheckInOut {
    CHECKIN, CHECKOUT, OTHER
}

enum class Currency {
    EUR,
    USD
}

val idGenerator = AtomicLong()

data class Segment(
    val id: Long = idGenerator.incrementAndGet(),
    val dateTime: LocalDateTime,
    val destination: String,
    val station: String,
    val check: CheckInOut,
    val cost: BigDecimal,
    val currency: Currency,
    val description: String? = null,
) {
    fun isWorkDay(): Boolean = !(dateTime.dayOfWeek == DayOfWeek.SATURDAY || dateTime.dayOfWeek == DayOfWeek.SUNDAY)
}

data class SegmentNode(
    val date: LocalDate? = null,
    val name: String,
    val description: String? = null,
)

data class DailyCost(
    val date: LocalDate,
    val description: String,
    val cost: BigDecimal,
)

class CalculatorDao(
    val notIncluded: List<String> = listOf("Arnhem", "Velp", "Schiphol"),
    val dailyCostLimit: BigDecimal = BigDecimal.TEN,
    val travelRoutes: List<List<SegmentNode>> = emptyList(),
) {

    val error = AtomicBoolean(false)

    val ovPublicTransporParser: OVPublicTransporParser by lazy { OVPublicTransporParser() }

    /**
     * Both source and destination stations are shown in the logs
     */
    fun dailyCosts(fileUrl: URL, all: Boolean) = run {
        val allSegments = findAllSegments(fileUrl, all)
        error.set(ovPublicTransporParser.error.get())
        val filteredSegmentList = filterAllSegments(allSegments)
        logger.info(">>>>> Pay Segments")
        filteredSegmentList.sortedBy { it.dateTime }
            .toSet()
            .asSequence()
            .groupBy { it.dateTime.toLocalDate() }
            .map {
                logger.info(it.allRoutesMessage())
                it.toSumOfAllCosts()
            }
            .filter {
                it.cost > dailyCostLimit
            }
            .onEach { logger.info(it) }
            .toList()
    }

    internal fun filterAllSegments(allSegments: List<Segment>): MutableList<Segment> {
        val segmentList = allSegments.sortedBy { it.dateTime }

        val filteredSegmentList = mutableListOf<Segment>()
        val currentTestList = mutableListOf<Segment>()
        val forward = AtomicBoolean(true)

        if (travelRoutes.isEmpty()) {
            filteredSegmentList.addAll(allSegments)
        } else {
            val segmentsGroupedByDay = segmentList.groupBy { it.dateTime.toLocalDate() }
            travelRoutes.forEach { travelRoute ->
                segmentsGroupedByDay
                    .forEach { (_, segmentPerDay) ->
                        if (travelRoute[0].date == null || travelRoute[0].date == segmentPerDay[0].dateTime.toLocalDate()) {
                            segmentPerDay.forEach { segment ->
                                if (forward.get()) {
                                    if (segment.destination.contains(travelRoute[0].name)) {
                                        if (segment.station.contains(travelRoute[1].name)) {
                                            forward.set(false)
                                            filteredSegmentList.add(segment, travelRoute[0].description)
                                        } else {
                                            currentTestList.add(segment)
                                        }
                                    } else if (segment.station.contains(travelRoute[1].name)) {
                                        if (currentTestList.size > 0) {
                                            filteredSegmentList.addAll(currentTestList, travelRoute[0].description)
                                            filteredSegmentList.add(segment, travelRoute[0].description)
                                            currentTestList.clear()
                                        }
                                        forward.set(false)
                                    } else if (currentTestList.isNotEmpty()) currentTestList.add(segment)
                                } else if (!forward.get()) {
                                    if (segment.destination.contains(travelRoute[1].name)) {
                                        if (segment.station.contains(travelRoute[0].name)) {
                                            filteredSegmentList.add(segment, travelRoute[0].description)
                                            forward.set(true)
                                        } else {
                                            currentTestList.add(segment)
                                        }

                                    } else if (segment.station.contains(travelRoute[0].name)) {
                                        if (currentTestList.size > 0) {
                                            filteredSegmentList.addAll(currentTestList, travelRoute[0].description)
                                            filteredSegmentList.add(segment, travelRoute[0].description)
                                            currentTestList.clear()
                                        }
                                        forward.set(true)
                                    } else if (currentTestList.isNotEmpty()) currentTestList.add(segment)
                                }
                            }
                        }
                        forward.set(true)
                        currentTestList.clear()
                    }
            }
        }
        return filteredSegmentList
    }

    private fun findAllSegments(fileUrl: URL, all: Boolean) = ovPublicTransporParser.parseDocument(fileUrl, all)

    private fun Map.Entry<LocalDate, List<Segment?>>.toSumOfAllCosts() = DailyCost(
        date = this.key,
        cost = this.value.sumOf { segment ->
            if (segment.notIncluded()) BigDecimal.ZERO else
                segment?.cost ?: BigDecimal.ZERO
        },
        description = this.value.last()?.description ?: "Event description here"
    )

    private inline fun <reified K : LocalDate?, V : List<Segment?>> Map.Entry<K, V>.allRoutesMessage(): String =
        "${this.key} - ${this.value.joinToString(" -> ") { segment -> "${segment?.destination} - ${segment?.station}" }}"


    private fun Segment?.notIncluded(): Boolean = notIncluded.firstOrNone { not ->
        this?.station?.contains(not) == true || this?.destination?.contains(not) == true
    }.isSome()

    companion object {
        private val logger = object {
            fun info(text: Any) = println(text)
        }
    }

}

private fun MutableList<Segment>.addAll(list: MutableList<Segment>, description: String?) =
    addAll(list.map { it.copy(description = description) })

private fun MutableList<Segment>.add(element: Segment, description: String?) =
    add(element.copy(description = description))
