package net.igorbrejc.timeslack

data class RunningPlanModel(
    val plan: SlackerPlan,
    val activitiesLog: SlackerActivitiesLog,
    val currentTime: SlackerTime
) {
    fun planFinishTime(): SlackerTime {
        currentTime.diffFrom(activitiesLog.currentActivityStartTime())

        val totalDurationInMinutes =
            currentActivity().durationInMinutes +
            remainingActivities().sumBy { it.durationInMinutes }

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
