package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Running plan model` {
    @Test
    fun `Calculates plan finish time`() {
        val model: RunningPlanModel = RunningPlanModelBuilder()
            .givenAPlan()
            .andCurrentTimeOf(10, 34)
            .build()
        assertEquals(SlackerTime.of(12, 55), model.FinishTime)
    }
}