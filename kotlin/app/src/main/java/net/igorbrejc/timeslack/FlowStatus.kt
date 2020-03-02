package net.igorbrejc.timeslack

sealed class FlowStatus

data class FlowRunningWithMoreActivities(
    val currentStep: FlowStep,
    val currentActivity: FlowActivity,
    val nextActivity: FlowActivity
) : FlowStatus()

data class FlowRunningLastActivity(
    val currentStep: FlowStep,
    val currentActivity: FlowActivity
) : FlowStatus()

object FlowFinished : FlowStatus()
