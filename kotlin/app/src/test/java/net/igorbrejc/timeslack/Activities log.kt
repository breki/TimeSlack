package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Activities log` {
    @Test
    fun `returns the plan start time as the activity start time if first activity`() {
        val planStartTime = SlackerTime.of(10, 44)
        val log = SlackerActivitiesLog(planStartTime, emptyList());
        assertEquals(planStartTime, log.currentActivityStartTime())
    }

    @Test
    fun `returns the recorded activity start time if not the first activity`() {
        val planStartTime = SlackerTime.of(10, 44)
        val activityStartTime = planStartTime.add(SlackerDuration(15))
        val log = SlackerActivitiesLog(
            planStartTime, ImmutableList.of(activityStartTime));
        assertEquals(activityStartTime, log.currentActivityStartTime())
    }
}