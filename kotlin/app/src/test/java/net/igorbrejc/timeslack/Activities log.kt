package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class `Activities log` {
    @Test
    fun `requires at least one time point`() {
        assertThrows(IllegalArgumentException::class.java) {
            ActivitiesLog(10, emptyList())
        }
    }

    @Test
    fun `cannot specify more time points than there are activities (plus 1)`() {
        assertThrows(IllegalArgumentException::class.java) {
            ActivitiesLog(
                2,
                ImmutableList.of(
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20)))
        }
    }

    @Test
    fun `returns the flow start time as the activity start time if first activity`() {
        val flowStartTime = SlackerTime.of(10, 44)
        val log = ActivitiesLog(10, ImmutableList.of(flowStartTime))
        assertEquals(flowStartTime, log.currentActivityStartTime())
    }

    @Test
    fun `returns the recorded activity start time if not the first activity`() {
        val flowStartTime = SlackerTime.of(10, 44)
        val activityStartTime = flowStartTime.add(SlackerDuration(15))
        val log = ActivitiesLog(
            10,
            ImmutableList.of(flowStartTime, activityStartTime))
        assertEquals(activityStartTime, log.currentActivityStartTime())
    }
}