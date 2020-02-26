package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Activities log` {
    @Test
    fun `returns the flow start time as the activity start time if first activity`() {
        val flowStartTime = SlackerTime.of(10, 44)
        val log = ActivitiesLog(flowStartTime, emptyList())
        assertEquals(flowStartTime, log.currentActivityStartTime())
    }

    @Test
    fun `returns the recorded activity start time if not the first activity`() {
        val flowStartTime = SlackerTime.of(10, 44)
        val activityStartTime = flowStartTime.add(SlackerDuration(15))
        val log = ActivitiesLog(
            flowStartTime, ImmutableList.of(activityStartTime))
        assertEquals(activityStartTime, log.currentActivityStartTime())
    }
}