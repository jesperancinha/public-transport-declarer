package org.jesperancinha.ptd.daily

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

enum class TransportType(val nlName: String) {
    TRAM_BUS("tram/bus"), TRAM("tram"), BUS("bus"), TRAIN("trein"), OTHER("other")
}

data class Segment(
    val dateTime: LocalDateTime,
    val station: String,
    val type: TransportType,
    val check: CheckInOut,
    val cost: BigDecimal = BigDecimal.ZERO,
    val currency: String = "EUR"
)

enum class CheckInOut {
    CHECKIN, CHECKOUT
}

data class Journey(
    val checkIn: Segment,
    val checkOut: Segment?,
    val type: TransportType,
    val isComplete: Boolean = checkOut != null
) {
    val duration: Duration
        get() = if (checkOut?.dateTime != null) {
            Duration.between(checkIn.dateTime, checkOut.dateTime)
        } else {
            Duration.ZERO
        }
}

data class DailyJourney(
    val completeJourneys: List<Journey>,
    val missedCheckoutSegments: List<Segment>,
)

fun Duration.toDurationString(): String {
    val hours = toHours()
    val minutes = toMinutesPart()
    val seconds = toSecondsPart()
    return String.format("%02dh %02dm %02ds", hours, minutes, seconds)
}
