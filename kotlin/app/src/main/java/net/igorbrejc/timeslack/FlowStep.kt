package net.igorbrejc.timeslack

sealed class FlowStep (open val stepName: String)

data class FixedActivityStep (override val stepName: String)
    : FlowStep(stepName)

data class DynamicActivityForwardStep (override val stepName: String)
    : FlowStep(stepName)

data class DynamicActivityReturnStep (override val stepName: String)
    : FlowStep(stepName)
