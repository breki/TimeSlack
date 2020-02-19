package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

class RunningPlanModelBuilder {
    fun givenAPlan(): RunningPlanModelBuilder {
        plan = SlackerPlan(
            ImmutableList.of(
                SlackerActivity("prepare things", 10),
                SlackerActivity("drive", 15),
                SlackerActivity("prepare for hiking", 5),
                SlackerActivity("hiking", 60),
                SlackerActivity("unpack", 5),
                SlackerActivity("drive", 15),
                SlackerActivity("wash & dress", 15)
            ))

        return this
    }

    fun andCurrentActivity(activityIndex: Int, activityStartTime: SlackerTime):
            RunningPlanModelBuilder {
        log = when (activityIndex) {
            0 -> SlackerActivitiesLog(activityStartTime, emptyList())
            else -> SlackerActivitiesLog(
                activityStartTime,
                (0..activityIndex).map { activityStartTime })
        }

        return this
    }

    fun andCurrentTimeOf(hours: Int, minutes: Int): RunningPlanModelBuilder {
        currentTime = SlackerTime.of(hours, minutes)
        return this
    }

    fun andCurrentTimeOf(time: SlackerTime): RunningPlanModelBuilder {
        currentTime = time
        return this
    }

    fun build(): RunningPlanModel {
        return RunningPlanModel(plan!!, log!!, currentTime!!)
    }

    private var plan: SlackerPlan? = null
    private var log: SlackerActivitiesLog? = null
    private var currentTime: SlackerTime? = null
}
