package net.igorbrejc.timeslack

import java.lang.IllegalStateException
import kotlin.math.max

data class RunningFlowModel(
    val flow: Flow,
    val deadline: SlackerTime,
    val flowLog: FlowLog,
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

    fun currentActivityInfo(): CurrentActivityInfo {
        return when (val activity = currentActivity()) {
            is FixedActivity -> {
                val plannedFinishTime = when {
                    currentActivityDuration()
                        .isGreaterThan(activity.expectedDuration)
                    -> currentTime
                    else -> activitiesLog.currentActivityStartTime()
                        .add(activity.expectedDuration)
                }

                val remainingDuration = when {
                    currentActivityDuration()
                        .isGreaterThan(activity.expectedDuration)
                    -> SlackerDuration.zero
                    else -> activity.expectedDuration
                        .diff(currentActivityDuration())
                }

                CurrentFixedActivityInfo(
                    activity.activityName,
                    remainingDuration,
                    plannedFinishTime)
            }

            is DynamicActivity -> TODO()
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
        return when (val activity = currentActivity()) {
            is FixedActivity -> {
                // if the current activity is running longer than the allotted time,
                // use the actual running time instead
                val actualCurrentActivityRunningTimeInMinutes =
                    currentActivityDuration().durationInMinutes

                val currentActivityRunningTimeInMinutes: Int =
                    max(
                        actualCurrentActivityRunningTimeInMinutes,
                        activity.expectedDuration.durationInMinutes
                    )

                val totalDurationInMinutes =
                    currentActivityRunningTimeInMinutes +
                    remainingActivitiesExpectedDuration().durationInMinutes

                activitiesLog.currentActivityStartTime()
                    .add(SlackerDuration(totalDurationInMinutes))
            }

            is DynamicActivity -> TODO()
        }
    }

    private fun currentActivity(): FlowActivity {
        return flow.activities[activitiesLog.currentActivityIndex()]
    }

    private fun remainingActivities(): List<FlowActivity> {
        return flow.activities.drop(activitiesLog.currentActivityIndex() + 1)
    }

    private fun remainingActivitiesExpectedDuration(): SlackerDuration {
        val expectedDurations =
            remainingActivities().map { activity ->
                when (activity) {
                    is FixedActivity -> activity.expectedDuration
                    is DynamicActivity -> TODO()
                }
            }

        return SlackerDuration.sumOf(expectedDurations)
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
        return RunningFlowModel(
            flow, deadline, flowLog, activitiesLog, currentTime)
    }

    fun finishCurrentActivity(currentTime: SlackerTime): RunningFlowModel {
        return RunningFlowModel(
            flow,
            deadline,
            TODO(),
            activitiesLog.finishActivity(currentTime),
            currentTime)
    }
}
