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

    fun andPlanStartTime(startTime: SlackerTime): RunningPlanModelBuilder {
        planStartTime = startTime
        return this
    }

    fun andCurrentActivity(activityIndex: Int, activityStartTime: SlackerTime) {
        throw NotImplementedError("");
    }

    fun andCurrentTimeOf(hours: Int, minutes: Int): RunningPlanModelBuilder {
        currentTime = SlackerTime.of(hours, minutes)
        return this
    }

    fun build(): RunningPlanModel {
        return RunningPlanModel(plan!!, currentTime!!)
    }

    private var plan: SlackerPlan? = null
    private var planStartTime: SlackerTime? = null
    private var currentTime: SlackerTime? = null
}

