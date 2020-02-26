package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Flow status is` {
    @Test
    fun `RunningWithMoreActivities when there are still some remaining activities`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(2, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            FlowRunningWithMoreActivities(
                FlowActivity("prepare for hiking", SlackerDuration(5)),
                FlowActivity("hiking", SlackerDuration(60))),
            model.flowStatus())
    }

    @Test
    fun `RunningLastActivity when the last activity is running`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(6, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            FlowRunningLastActivity(
                FlowActivity("wash & dress", SlackerDuration(15))),
            model.flowStatus())
    }

    @Test
    fun `Finished when the last activity has finished`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(7, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(FlowFinished, model.flowStatus())
    }
}