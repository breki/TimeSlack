package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

class RunningPlanModelBuilder {
    fun givenAPlan(): RunningPlanModelBuilder {
        plan = SlackerPlan(
            ImmutableList.of(
                SlackerActivity("prepare things", SlackerDuration(10)),
                SlackerActivity("drive", SlackerDuration(15)),
                SlackerActivity("prepare for hiking", SlackerDuration(5)),
                SlackerActivity("hiking", SlackerDuration(60)),
                SlackerActivity("unpack", SlackerDuration(5)),
                SlackerActivity("drive", SlackerDuration(15)),
                SlackerActivity("wash & dress", SlackerDuration(15))
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
        return RunningPlanModel(plan!!, deadline!!, log!!, currentTime!!)
    }

    fun andDeadlineOf(deadline: SlackerTime): RunningPlanModelBuilder {
        this.deadline = deadline
        return this
    }

    private var plan: SlackerPlan? = null
    private var deadline: SlackerTime? = null
    private var log: SlackerActivitiesLog? = null
    private var currentTime: SlackerTime? = null
}
