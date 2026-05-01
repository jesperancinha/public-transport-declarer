package org.jesperancinha.ptd.daily

import java.math.BigDecimal
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
)
