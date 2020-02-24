package net.igorbrejc.timeslack

sealed class SlackerPlanStatus

data class PlanRunningWithMoreActivities(
    val currentActivity: SlackerActivity,
    val nextActivity: SlackerActivity
) : SlackerPlanStatus()

data class PlanRunningLastActivity(
    val currentActivity: SlackerActivity
) : SlackerPlanStatus()

object PlanFinished : SlackerPlanStatus()
