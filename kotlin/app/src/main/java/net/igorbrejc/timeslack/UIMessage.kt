package net.igorbrejc.timeslack

sealed class UIMessage

data class TimerUpdated(val currentTime: SlackerTime) : UIMessage()

data class NextActivityButtonClicked(val currentTime: SlackerTime) :
    UIMessage()
