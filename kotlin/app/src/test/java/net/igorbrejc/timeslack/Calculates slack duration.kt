package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Calculates slack duration` {
    @Test
    fun `to zero if flow finish time is beyond deadline`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(0, model.slackDuration().durationInMinutes)
    }

    @Test
    fun `as greater than zero if flow finish time is before deadline`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(10))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(200)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        Assertions.assertEquals(75, model.slackDuration().durationInMinutes)
    }
}