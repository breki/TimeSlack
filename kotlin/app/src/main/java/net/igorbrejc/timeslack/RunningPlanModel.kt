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
            currentActivityDuration().durationInMinutes

        val currentActivityRunningTimeInMinutes =
            max(
                actualCurrentActivityRunningTimeInMinutes,
                currentActivity().expectedDuration.durationInMinutes)

        val totalDurationInMinutes =
            currentActivityRunningTimeInMinutes +
            + remainingActivities().sumBy {
                it.expectedDuration.durationInMinutes }

        return activitiesLog.currentActivityStartTime()
            .add(SlackerDuration(totalDurationInMinutes))
    }

    fun currentActivityRemainingDuration(): SlackerDuration {
        return when {
            currentActivityDuration()
                .isGreaterThan(currentActivity().expectedDuration)
                -> SlackerDuration.zero
            else -> currentActivity().expectedDuration
                .diff(currentActivityDuration())
        }
    }

    fun currentActivityFinishTime(): SlackerTime {
        return when {
            currentActivityDuration()
                .isGreaterThan(currentActivity().expectedDuration)
            -> currentTime
            else -> activitiesLog.currentActivityStartTime()
                .add(currentActivity().expectedDuration)
        }
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

    private fun currentActivityDuration(): SlackerDuration {
        return currentTime.diffFrom(activitiesLog.currentActivityStartTime())
    }
}
