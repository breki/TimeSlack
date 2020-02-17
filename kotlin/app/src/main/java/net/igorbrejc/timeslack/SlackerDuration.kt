package net.igorbrejc.timeslack

import java.lang.IllegalArgumentException

data class SlackerDuration (val durationInMinutes: Int) {
    init {
        when {
            durationInMinutes <= 0 -> throw IllegalArgumentException()
        }
    }
}
