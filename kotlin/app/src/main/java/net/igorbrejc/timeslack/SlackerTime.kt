package net.igorbrejc.timeslack

import java.util.*

data class SlackerTime internal constructor (private val minutesOfDay: Int) {
    val hours: Int = minutesOfDay / 60
    val minutes = minutesOfDay % 60

    companion object {
        fun of(hours: Int, minutes: Int): SlackerTime {
            return when {
                hours < 0 -> throw IllegalArgumentException()
                hours > 23 -> throw IllegalArgumentException()
                minutes < 0 -> throw IllegalArgumentException()
                minutes > 59 -> throw IllegalArgumentException()
                else -> SlackerTime(hours * 60 + minutes)
            }
        }

        fun now(): SlackerTime {
            val now = Calendar.getInstance()
            val hours: Int = now.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = now.get(Calendar.MINUTE)
            return of(hours, minutes)
        }
    }

    fun add(minutesToAdd: Int): SlackerTime {
        return SlackerTime(minutesOfDay + minutesToAdd)
    }

    fun diffFrom(other: SlackerTime): Int {
        return this.minutesOfDay - other.minutesOfDay
    }

    override fun toString(): String {
        return "%d:%02d".format(hours, minutes)
    }
}
