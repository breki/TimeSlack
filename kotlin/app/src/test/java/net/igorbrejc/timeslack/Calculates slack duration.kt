package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Calculates slack duration` {
    @Test
    fun `to zero if plan finish time is beyond deadline`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(10)

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(startTime.add(20))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(0, model.slackDuration().durationInMinutes)
    }

    @Test
    fun `as greater than zero if plan finish time is before deadline`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(10)

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(startTime.add(200))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(75, model.slackDuration().durationInMinutes)
    }
}