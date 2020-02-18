package net.igorbrejc.timeslack

data class RunningPlanModel(
    val plan: SlackerPlan,
    val activitiesLog: SlackerActivitiesLog,
    val currentTime: SlackerTime) {

    init {
    }

    fun currentActivity(): SlackerActivity {
        return plan.activities[activitiesLog.currentActivityIndex()]
    }

    fun planFinishTime(): SlackerTime {
        val totalDurationInMinutesForRemainingActivities =
            remainingActivities().sumBy { it.durationInMinutes }
        val totalDurationInMinutesIncludingCurrentActivity =
            totalDurationInMinutesForRemainingActivities
            + currentActivity().durationInMinutes

        return activitiesLog.currentActivityStartTime()
            .add(totalDurationInMinutesForRemainingActivities)
    }

    private fun remainingActivities(): List<SlackerActivity> {
        return plan.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }
}