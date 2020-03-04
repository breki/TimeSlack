package net.igorbrejc.timeslack.views

import net.igorbrejc.timeslack.SlackerDuration
import net.igorbrejc.timeslack.SlackerTime

data class RunningFlowView(
    val forwardButton: ForwardButtonType,
    val currentStep: CurrentStepView?,
    val nextStep: NextStepView?,
    val slackDuration: SlackerDuration,
    val flowFinishTime: SlackerTime
)
