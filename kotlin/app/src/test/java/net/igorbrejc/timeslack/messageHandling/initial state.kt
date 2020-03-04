package net.igorbrejc.timeslack.messageHandling

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import net.igorbrejc.timeslack.*
import net.igorbrejc.timeslack.state.RunningFlowState
import net.igorbrejc.timeslack.views.CurrentStepView
import net.igorbrejc.timeslack.views.ForwardButtonType
import net.igorbrejc.timeslack.views.NextStepView
import net.igorbrejc.timeslack.views.RunningFlowView
import org.junit.jupiter.api.Test

class `initial state` {
    @Test
    fun `first step name is shown`() {
        val (_, view) = startAFlow()
        assertThat(view.currentStep, present())
        assertThat(view.currentStep?.name, equalTo("prepare things"))
    }

    @Test
    fun `first dynamic step name is shown if the first activity is dynamic`() {
        val (_, view) = startAFlowWithDynamicActivity()
        assertThat(view.currentStep, present())
        assertThat(view.currentStep?.name, equalTo("hike"))
    }

    @Test
    fun `next step name is shown if there is more than one step`() {
        val (_, view) = startAFlow()
        assertThat(view.nextStep, present())
        assertThat(view.nextStep?.name, equalTo("drive"))
    }

    @Test
    fun `next step name is not shown if there is only one step`() {
        val (_, view) = startAFlowWithASingleStep()
        assertThat(view.nextStep, absent())
    }

    @Test
    fun `next activity button is shown if first activity is fixed`() {
        val (_, view) = startAFlow()
        assertThat(view.forwardButton, equalTo(ForwardButtonType.NextActivity))
    }

    @Test
    fun `start returning button is shown if first activity is dynamic`() {
        val (_, view) = startAFlowWithDynamicActivity()
        assertThat(
            view.forwardButton, equalTo(ForwardButtonType.StartReturning))
    }

    private fun startAFlow() : StateAndView<RunningFlowState, RunningFlowView> {
        val flow = FlowBuilder().withSampleActivities().build()
        val deadline = SlackerTime.of(14, 33)
        val currentTime = SlackerTime.of(12, 44)

        return RunningFlowState.startFlow(flow, deadline, currentTime)
    }

    private fun startAFlowWithDynamicActivity() :
            StateAndView<RunningFlowState, RunningFlowView> {
        val flow = FlowBuilder()
            .addActivity(DynamicActivity("hike", SlackerDuration(30)))
            .build()
        val deadline = SlackerTime.of(14, 33)
        val currentTime = SlackerTime.of(12, 44)

        return RunningFlowState.startFlow(flow, deadline, currentTime)
    }

    private fun startAFlowWithASingleStep() :
            StateAndView<RunningFlowState, RunningFlowView> {
        val flow = FlowBuilder()
            .addActivity(FixedActivity("some step", SlackerDuration(10)))
            .build()
        val deadline = SlackerTime.of(14, 33)
        val currentTime = SlackerTime.of(12, 44)

        return RunningFlowState.startFlow(flow, deadline, currentTime)
    }
}