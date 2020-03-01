package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Calculates activity remaining duration` {
    @Test
    fun `to zero if the fixed activity duration is longer than allotted one`() {
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
                assertEquals(
                    SlackerDuration.zero, activityInfo.remainingDuration)
            }
        }
    }

    @Test
    fun `above zero if the fixed activity duration is shorter than allotted one`() {
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
                assertEquals(SlackerDuration(5), activityInfo.remainingDuration)
            }
        }
    }
}