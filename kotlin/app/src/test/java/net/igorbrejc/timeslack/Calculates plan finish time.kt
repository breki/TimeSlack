package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Calculates plan finish time` {
    @Test
    fun `when current activity is still within its allotted time`() {
        val activityAllottedTime = 10
        val activitySpentTime = activityAllottedTime - 5
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(activitySpentTime))
        val expectedFinishTime = SlackerTime.of(12, 25)

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(currentTime.add(SlackerDuration(200)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        assertEquals(expectedFinishTime, model.planFinishTime())
    }

    @Test
    fun `when current activity has passed its allotted time`() {
        val activityAllottedTime = 10
        val overtime = 5
        val activitySpentTime = activityAllottedTime + overtime
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(activitySpentTime))
        val expectedFinishTime =
            SlackerTime.of(12, 25).add(SlackerDuration(overtime))

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(currentTime.add(SlackerDuration(200)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()
        assertEquals(expectedFinishTime, model.planFinishTime())
    }

    @Test
    fun `when plan has already finished, returns the finish time of the last activity`() {
        val startTime = SlackerTime.of(10, 20)
        val finishTime = startTime.add(SlackerDuration(120))
        val currentTime = finishTime.add(SlackerDuration(30))

        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andDeadlineOf(currentTime.add(SlackerDuration(15)))
            .andCurrentActivity(7, finishTime)
            .andCurrentTimeOf(currentTime)
            .build()
        assertEquals(finishTime, model.planFinishTime())
    }
}
