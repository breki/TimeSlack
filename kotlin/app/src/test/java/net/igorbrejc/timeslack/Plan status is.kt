package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Plan status is` {
    @Test
    fun `RunningWithMoreActivities when there are still some remaining activities`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(2, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            PlanRunningWithMoreActivities(
                SlackerActivity("prepare for hiking", SlackerDuration(5)),
                SlackerActivity("hiking", SlackerDuration(60))),
            model.planStatus())
    }

    @Test
    fun `RunningLastActivity when the last activity is running`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(6, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(
            PlanRunningLastActivity(
                SlackerActivity("wash & dress", SlackerDuration(15))),
            model.planStatus())
    }

    @Test
    fun `Finished when the last activity has finished`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(7, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(PlanFinished, model.planStatus())
    }
}