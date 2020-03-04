package net.igorbrejc.timeslack

sealed class FlowStatus

data class FlowRunningWithMoreSteps(
    val currentStep: FlowStep,
    val nextStep: FlowStep
) : FlowStatus()

data class FlowRunningLastStep(
    val currentStep: FlowStep
) : FlowStatus()

object FlowFinished : FlowStatus()
