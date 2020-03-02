package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class `Flow log` {
    @Test
    fun `at least one step is required for the log`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            FlowLog(emptyList(), emptyList())
        }
    }

    @Test
    fun `at least one time point is required for the log`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            FlowLog(listOf(FixedActivityStep("sdsdd")), emptyList())
        }
    }

    @Test
    fun `there cannot be time points more than steps count plus 1`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            FlowLog(
                listOf(FixedActivityStep("sdsdd"), FixedActivityStep("sdsdd")),
                listOf(
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20),
                    SlackerTime.of(10, 20)))
        }
    }

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

    @Test
    fun `when flow log is created, it returns the first step as the current step`() {
        val flow = FlowBuilder()
            .addActivity(FixedActivity("prepare things", SlackerDuration(10)))
            .addActivity(FixedActivity("drive", SlackerDuration(15)))
            .build()

        val flowStartTime = SlackerTime.of(10, 22)
        val log = flow.startFlow(flowStartTime)

        assertEquals(
            FixedActivityStep("prepare things"),
            log.currentStep())
    }

    @Test
    fun `when a step is finished, the next step is the current step`() {
        val flow = FlowBuilder()
            .addActivity(FixedActivity("prepare things", SlackerDuration(10)))
            .addActivity(FixedActivity("drive", SlackerDuration(15)))
            .build()

        val flowStartTime = SlackerTime.of(10, 22)
        val log = flow.startFlow(flowStartTime)
            .finishStep(SlackerTime.of(10, 30))

        assertEquals(FixedActivityStep("drive"), log.currentStep())
    }
}