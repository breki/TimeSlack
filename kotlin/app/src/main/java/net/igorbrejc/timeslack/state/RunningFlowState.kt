package net.igorbrejc.timeslack.state

import net.igorbrejc.timeslack.*
import net.igorbrejc.timeslack.views.CurrentStepView
import net.igorbrejc.timeslack.views.ForwardButtonType
import net.igorbrejc.timeslack.views.NextStepView
import net.igorbrejc.timeslack.views.RunningFlowView
import java.lang.IllegalStateException

data class RunningFlowState internal constructor(
    val flow: Flow,
    val deadline: SlackerTime,
    val flowLog: FlowLog) {

    companion object {
        fun startFlow(
            flow: Flow,
            deadline: SlackerTime,
            currentTime: SlackerTime) :
                StateAndView<RunningFlowState, RunningFlowView> {
            val state = RunningFlowState(
                flow, deadline, flow.startFlow(currentTime))

            val (currentStepName, forwardButton) =
                when (val currentStep = state.flowLog.currentStep()) {
                    null -> throw IllegalStateException(
                        "BUG: this should never happen")
                    else -> {
                        val forwardButton = when (currentStep) {
                            is FixedActivityStep ->
                                ForwardButtonType.NextActivity
                            is DynamicActivityForwardStep ->
                                ForwardButtonType.StartReturning
                            is DynamicActivityReturnStep ->
                                ForwardButtonType.NextActivity
                        }
                        Pair(currentStep.stepName, forwardButton)
                    }
                }

            val nextStepView =
                when (val nextStep = state.flowLog.nextStep()) {
                    null -> null
                    else -> NextStepView(nextStep.stepName)
                }

            val view = RunningFlowView(
                forwardButton,
                CurrentStepView(
                    currentStepName,
                    SlackerDuration(100),
                    SlackerTime.of(22, 33)),
                nextStepView,
                SlackerDuration(100),
                SlackerTime.of(22, 33)
            )

            return StateAndView(state, view)
        }
    }
}