package net.igorbrejc.timeslack

import java.lang.IllegalStateException
import kotlin.math.max

data class RunningFlowModel(
    val flow: Flow,
    val deadline: SlackerTime,
    val activitiesLog: ActivitiesLog,
    val currentTime: SlackerTime
) {
    fun flowFinishTime(): SlackerTime {
        return when (flowStatus()) {
            is FlowRunningWithMoreActivities -> calculatedFlowFinishTime()
            is FlowRunningLastActivity -> calculatedFlowFinishTime()
            is FlowFinished -> activitiesLog.currentActivityStartTime()
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
        val finishTime = flowFinishTime()
        return when (finishTime.isAfter(deadline)) {
            true -> SlackerDuration.zero
            false -> deadline.diffFrom(finishTime)
        }
    }

    private fun calculatedFlowFinishTime(): SlackerTime {
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

    private fun currentActivity(): FlowActivity {
        return flow.activities[activitiesLog.currentActivityIndex()]
    }

    private fun remainingActivities(): List<FlowActivity> {
        return flow.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }

    private fun currentActivityDuration(): SlackerDuration {
        return currentTime.diffFrom(activitiesLog.currentActivityStartTime())
    }

    fun flowStatus(): FlowStatus {
        val currentActivityIndex = activitiesLog.currentActivityIndex()

        return when {
            currentActivityIndex < flow.activities.count() - 1 ->
                FlowRunningWithMoreActivities(
                    flow.activities[currentActivityIndex],
                    flow.activities[currentActivityIndex + 1])
            currentActivityIndex == flow.activities.count() - 1 ->
                FlowRunningLastActivity(
                    flow.activities[currentActivityIndex])
            currentActivityIndex == flow.activities.count() -> FlowFinished
            else -> throw IllegalStateException("BUG")
        }
    }

    fun withCurrentTime(currentTime: SlackerTime): RunningFlowModel {
        return RunningFlowModel(flow, deadline, activitiesLog, currentTime)
    }

    fun finishCurrentActivity(currentTime: SlackerTime): RunningFlowModel {
        return RunningFlowModel(
            flow,
            deadline,
            activitiesLog.finishActivity(currentTime),
            currentTime)
    }
}