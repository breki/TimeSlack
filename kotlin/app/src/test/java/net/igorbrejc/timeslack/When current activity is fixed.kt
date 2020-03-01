package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `When current activity is fixed` {
    @Test
    fun `remaining duration is zero if the activity duration is longer than allotted one`() {
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
                    SlackerDuration.zero, activityInfo.remainingDuration
                )
            }
        }
    }

    @Test
    fun `remaining duration is above zero if the activity duration is shorter than allotted one`() {
        val startTime = SlackerTime.of(10, 20)
        val currentTime = startTime.add(SlackerDuration(5))

        val model: RunningFlowModel = RunningFlowModelBuilder()
            .givenAFlow()
            .andDeadlineOf(startTime.add(SlackerDuration(20)))
            .andCurrentActivity(0, startTime)
            .andCurrentTimeOf(currentTime)
            .build()

        //        assertThat(activityInfo, isA<CurrentFixedActivityInfo>())

        when (val activityInfo = model.currentActivityInfo()) {
            is CurrentFixedActivityInfo -> {
                Assertions.assertEquals(
                    SlackerDuration(5),
                    activityInfo.remainingDuration
                )
            }
        }
    }

    @Test
    fun `the activity finish time equals to activity start time plus allotted duration when the activity duration is not yet longer than allotted`() {
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
    fun `the activity finish time equals to current time when the activity duration is longer than allotted`() {
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
