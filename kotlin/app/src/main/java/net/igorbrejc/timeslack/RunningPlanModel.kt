package net.igorbrejc.timeslack

import kotlin.math.max

data class RunningPlanModel(
    val plan: SlackerPlan,
    val deadline: SlackerTime,
    val activitiesLog: SlackerActivitiesLog,
    val currentTime: SlackerTime
) {
    fun planFinishTime(): SlackerTime {
        // if the current activity is running longer than the allotted time,
        // use the actual running time instead
        val actualCurrentActivityRunningTimeInMinutes =
            currentTime.diffFrom(
                activitiesLog.currentActivityStartTime()).durationInMinutes

        val currentActivityRunningTimeInMinutes =
            max(
                actualCurrentActivityRunningTimeInMinutes,
                currentActivity().durationInMinutes)

        val totalDurationInMinutes =
            currentActivityRunningTimeInMinutes +
            + remainingActivities().sumBy { it.durationInMinutes }

        return activitiesLog.currentActivityStartTime()
            .add(totalDurationInMinutes)
    }

    fun currentActivity(): SlackerActivity {
        return plan.activities[activitiesLog.currentActivityIndex()]
    }

    fun nextActivity(): SlackerActivity? {
        val nextActivityIndex = activitiesLog.currentActivityIndex() + 1
        return when (nextActivityIndex < plan.activities.count()) {
            true -> plan.activities[nextActivityIndex]
            false -> null
        }
    }

    fun slackDuration(): SlackerDuration {
        val finishTime = planFinishTime()
        return when (finishTime.isAfter(deadline)) {
            true -> SlackerDuration.zero
            false -> deadline.diffFrom(finishTime)
        }
    }

    private fun remainingActivities(): List<SlackerActivity> {
        return plan.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }
}
