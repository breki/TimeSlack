package net.igorbrejc.timeslack

sealed class CurrentActivityInfo(open val activityName: String)

data class CurrentFixedActivityInfo(
    override val activityName: String,
    val remainingDuration: SlackerDuration,
    val plannedFinishTime: SlackerTime
) : CurrentActivityInfo(activityName)
