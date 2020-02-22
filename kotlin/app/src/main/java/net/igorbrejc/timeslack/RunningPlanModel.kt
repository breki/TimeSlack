package net.igorbrejc.timeslack

import kotlin.math.max

data class RunningPlanModel(
    val plan: SlackerPlan,
    val activitiesLog: SlackerActivitiesLog,
    val currentTime: SlackerTime
) {
    fun planFinishTime(): SlackerTime {
        // if the current activity is running longer than the allotted time,
        // use the actual running time instead
        val currentActivityRunningTimeInMinutes =
            max(
                currentTime.diffFrom(activitiesLog.currentActivityStartTime()),
                currentActivity().durationInMinutes)


        val totalDurationInMinutes =
            (currentActivityRunningTimeInMinutes
            + remainingActivities().sumBy { it.durationInMinutes })

        return activitiesLog.currentActivityStartTime()
            .add(totalDurationInMinutes)
    }

    private fun currentActivity(): SlackerActivity {
        return plan.activities[activitiesLog.currentActivityIndex()]
    }

    private fun remainingActivities(): List<SlackerActivity> {
        return plan.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }
}
