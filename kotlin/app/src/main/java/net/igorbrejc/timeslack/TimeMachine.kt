package net.igorbrejc.timeslack

import java.util.*

class TimeMachine constructor (
    private val speed: Double,
    private val startTime: SlackerTime? = null
) : Clock {
    override fun now(): SlackerTime {
        val systemNow = Calendar.getInstance()
        val actualTimePassedInSeconds =
            (systemNow.timeInMillis - systemStartTime.timeInMillis) / 1000
        val simulatedTimePassedInMinutes =
            actualTimePassedInSeconds * speed / 60

        return actualStartTime()
            .add(SlackerDuration(simulatedTimePassedInMinutes.toInt()))
    }

    private fun actualStartTime(): SlackerTime {
        return when (startTime) {
            null -> SlackerTime.fromCalendar(systemStartTime)
            else -> startTime
        }
    }

    private val systemStartTime = Calendar.getInstance()
}
