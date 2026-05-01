package org.jesperancinha.ptd.daily

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

enum class TransportType {
    TRAM, BUS, TRAIN, OTHER
}

data class Segment(
    val dateTime: LocalDateTime,
    val station: String,
    val type: TransportType,
    val check: CheckInOut,
    val cost: BigDecimal,
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

fun Duration.toDurationString(): String {
    val hours = toHours()
    val minutes = toMinutesPart()
    val seconds = toSecondsPart()
    return String.format("%02dh %02dm %02ds", hours, minutes, seconds)
}
