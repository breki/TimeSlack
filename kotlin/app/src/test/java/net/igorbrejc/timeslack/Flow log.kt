package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Flow log` {
    @Test
    fun `when flow is started the log translates activities into steps`() {
        val flow = FlowBuilder()
            .addActivity(FixedActivity("prepare things", SlackerDuration(10)))
            .addActivity(FixedActivity("drive", SlackerDuration(15)))
            .build()

        val flowStartTime = SlackerTime.of(10, 22)
        val log = flow.startFlow(flowStartTime)

        assertEquals(
            ImmutableList.of(
                FixedActivityStep("prepare things"),
                FixedActivityStep("drive")),
            log.steps)
    }

    @Test
    fun `dynamic activity is translated into two dynamic steps`() {
        val flow = FlowBuilder()
            .addActivity(FixedActivity("prepare things", SlackerDuration(10)))
            .addActivity(DynamicActivity("hike", SlackerDuration(30)))
            .build()

        val flowStartTime = SlackerTime.of(10, 22)
        val log = flow.startFlow(flowStartTime)

        assertEquals(
            ImmutableList.of(
                FixedActivityStep("prepare things"),
                DynamicActivityForwardStep("hike"),
                DynamicActivityReturnStep("hike (return)")),
            log.steps)
    }
}