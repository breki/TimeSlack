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
            return fromCalendar(now)
        }

        fun fromCalendar(calendar: Calendar): SlackerTime {
            val hours: Int = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = calendar.get(Calendar.MINUTE)
            return of(hours, minutes)
        }
    }

    fun add(duration: SlackerDuration): SlackerTime {
        return SlackerTime(minutesOfDay + duration.durationInMinutes)
    }

    fun diffFrom(other: SlackerTime): SlackerDuration {
        return SlackerDuration(this.minutesOfDay - other.minutesOfDay)
    }

    override fun toString(): String {
        return "%d:%02d".format(hours, minutes)
    }

    fun isAfter(other: SlackerTime): Boolean {
        return minutesOfDay > other.minutesOfDay
    }
}
