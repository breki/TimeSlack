package net.igorbrejc.timeslack

sealed class FlowActivity(open val activityName: String)

data class FixedActivity(
    override val activityName: String,
    val expectedDuration: SlackerDuration
) : FlowActivity(activityName)

data class DynamicActivity(
    override val activityName: String,
    val minimumDuration: SlackerDuration
) : FlowActivity(activityName)
