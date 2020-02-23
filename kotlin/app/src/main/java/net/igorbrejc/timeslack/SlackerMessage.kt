package net.igorbrejc.timeslack

sealed class SlackerMessage

data class TimerUpdated(val currentTime: SlackerTime) : SlackerMessage()

data class NextActivityButtonClicked(val currentTime: SlackerTime) :
    SlackerMessage()
