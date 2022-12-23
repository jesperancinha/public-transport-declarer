package org.jesperancinha.ptd.domain

import java.time.LocalDateTime

enum class CheckInOut {
    CHECKIN,
    CHECKOUT
}

data class Segment(
    val dateTime: LocalDateTime,
    val company: String,
    val station: String,
    val check: CheckInOut
)