package net.igorbrejc.timeslack

sealed class CurrentActivityInfo(open val activityName: String)

data class CurrentFixedActivityInfo(
    override val activityName: String,
    val remainingDuration: SlackerDuration,
    val plannedFinishTime: SlackerTime
) : CurrentActivityInfo(activityName)

data class CurrentDynamicActivityForwardInfo(
    override val activityName: String,
    val remainingDurationBeforeTurningBack: SlackerDuration,
    val plannedTimeBeforeTurningBack: SlackerTime
) : CurrentActivityInfo(activityName)
