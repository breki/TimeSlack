package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `Calculates activity finish time` {
    @Test
    fun `to activity start time plus allotted duration when the activity duration is not yet longer than allotted`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(5))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()

        when (val activityInfo = model.currentActivityInfo()) {
            is CurrentFixedActivityInfo -> {
                Assertions.assertEquals(
                    startTime.add(SlackerDuration(10)),
                    activityInfo.plannedFinishTime)
            }
        }
    }

    @Test
    fun `to current time when the activity duration is longer than allotted`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(30))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()

        when (val activityInfo = model.currentActivityInfo()) {
            is CurrentFixedActivityInfo -> {
                Assertions.assertEquals(
                    currentTime, activityInfo.plannedFinishTime)
            }
        }
    }
}