package net.igorbrejc.timeslack

import java.lang.IllegalStateException
import kotlin.math.max

data class RunningPlanModel(
    val plan: SlackerPlan,
    val deadline: SlackerTime,
    val activitiesLog: SlackerActivitiesLog,
    val currentTime: SlackerTime
) {
    fun planFinishTime(): SlackerTime {
        return when (planStatus()) {
            is PlanRunningWithMoreActivities -> calculatedPlanFinishTime()
            is PlanRunningLastActivity -> calculatedPlanFinishTime()
            is PlanFinished -> activitiesLog.currentActivityStartTime()
        }
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

    fun slackDuration(): SlackerDuration {
        val finishTime = planFinishTime()
        return when (finishTime.isAfter(deadline)) {
            true -> SlackerDuration.zero
            false -> deadline.diffFrom(finishTime)
        }
    }

    private fun calculatedPlanFinishTime(): SlackerTime {
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

    private fun currentActivity(): SlackerActivity {
        return plan.activities[activitiesLog.currentActivityIndex()]
    }

    private fun remainingActivities(): List<SlackerActivity> {
        return plan.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }

    private fun currentActivityDuration(): SlackerDuration {
        return currentTime.diffFrom(activitiesLog.currentActivityStartTime())
    }

    fun planStatus(): SlackerPlanStatus {
        val currentActivityIndex = activitiesLog.currentActivityIndex()

        return when {
            currentActivityIndex < plan.activities.count() - 1 ->
                PlanRunningWithMoreActivities(
                    plan.activities[currentActivityIndex],
                    plan.activities[currentActivityIndex + 1])
            currentActivityIndex == plan.activities.count() - 1 ->
                PlanRunningLastActivity(
                    plan.activities[currentActivityIndex])
            currentActivityIndex == plan.activities.count() -> PlanFinished
            else -> throw IllegalStateException("BUG")
        }
    }

    fun withCurrentTime(currentTime: SlackerTime): RunningPlanModel {
        return RunningPlanModel(plan, deadline, activitiesLog, currentTime)
    }

    fun finishCurrentActivity(currentTime: SlackerTime): RunningPlanModel {
        return RunningPlanModel(
            plan,
            deadline,
            activitiesLog.finishActivity(currentTime),
            currentTime)
    }
}
