package net.igorbrejc.timeslack

import java.lang.IllegalArgumentException

data class SlackerDuration(val durationInMinutes: Int) {
    init {
        when {
            durationInMinutes < 0 -> throw IllegalArgumentException()
        }
    }

    companion object {
        val zero = SlackerDuration(0)
    }

    fun isGreaterThan(other: SlackerDuration): Boolean =
        durationInMinutes > other.durationInMinutes

    fun diff(other: SlackerDuration): SlackerDuration =
        SlackerDuration(durationInMinutes - other.durationInMinutes)

    override fun toString(): String = "$durationInMinutes"
}
