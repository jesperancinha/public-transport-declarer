package org.jesperancinha.ptd.domain

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