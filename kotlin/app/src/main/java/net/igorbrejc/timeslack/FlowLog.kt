package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList
import java.lang.IllegalArgumentException

data class FlowLog(
    val steps: List<FlowStep>,
    val timePoints: List<SlackerTime>
) {
    init {
        when (steps.count()) {
            0 -> throw IllegalArgumentException(
                "At least one step is required.")
        }

        when {
            timePoints.count() == 0 -> throw IllegalArgumentException(
                "At least one time point is required.")
            timePoints.count() > steps.count() + 1 ->
                throw IllegalArgumentException(
                    "Too many time point for the given steps.")
        }
    }

    fun currentStep(): FlowStep? {
        return steps[timePoints.count() - 1]
    }

    fun nextStep(): FlowStep? {
        return when {
            timePoints.count() < steps.count() ->
                steps[timePoints.count()]
            else -> null
        }
    }

    fun finishStep(finishTime: SlackerTime): FlowLog {
        return FlowLog(
            steps,
            ImmutableList.builder<SlackerTime>()
                .addAll(timePoints).add(finishTime)
                .build())
    }
}
