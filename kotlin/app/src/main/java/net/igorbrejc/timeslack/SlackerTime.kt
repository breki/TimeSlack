package net.igorbrejc.timeslack

import java.lang.IllegalArgumentException

data class SlackerTime internal constructor (private val minutesOfDay: Int) {
    val hours: Int = minutesOfDay / 60
    val minutes = minutesOfDay % 60

    companion object {
        fun of (hours: Int, minutes: Int): SlackerTime {
            return when {
                hours < 0 -> throw IllegalArgumentException()
                hours > 23 -> throw IllegalArgumentException()
                minutes < 0 -> throw IllegalArgumentException()
                minutes > 59 -> throw IllegalArgumentException()
                else -> SlackerTime(hours * 60 + minutes)
            }
        }
    }
}