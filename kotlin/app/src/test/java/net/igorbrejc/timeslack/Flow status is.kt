package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Flow status is` {
    @Test
    fun `RunningWithMoreSteps when there are still some remaining steps`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentStep(2, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            FlowRunningWithMoreSteps(
                FixedActivityStep("prepare for hiking"),
                FixedActivityStep("hiking")),
            model.flowStatus())
    }

    @Test
    fun `RunningLastStep when the last step is running`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentStep(6, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            FlowRunningLastStep(
                FixedActivityStep("wash & dress")),
            model.flowStatus())
    }

    @Test
    fun `Finished when the last step has finished`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentStep(7, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(FlowFinished, model.flowStatus())
    }
}