package net.igorbrejc.timeslack.views

import net.igorbrejc.timeslack.SlackerDuration
import net.igorbrejc.timeslack.SlackerTime

data class CurrentStepView(
    val name: String,
    val duration: SlackerDuration,
    val plannedFinishTime: SlackerTime
)
