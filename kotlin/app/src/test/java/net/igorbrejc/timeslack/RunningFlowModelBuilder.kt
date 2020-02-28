package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

class RunningFlowModelBuilder {
    fun givenAFlow(): RunningFlowModelBuilder {
        flow = Flow(
            ImmutableList.of(
                FixedActivity("prepare things", SlackerDuration(10)),
                FixedActivity("drive", SlackerDuration(15)),
                FixedActivity("prepare for hiking", SlackerDuration(5)),
                FixedActivity("hiking", SlackerDuration(60)),
                FixedActivity("unpack", SlackerDuration(5)),
                FixedActivity("drive", SlackerDuration(15)),
                FixedActivity("wash & dress", SlackerDuration(15))
            ))

        return this
    }

    fun andCurrentActivity(activityIndex: Int, activityStartTime: SlackerTime):
            RunningFlowModelBuilder {
        log = ActivitiesLog(
            flow!!.activities.count(),
            (0..activityIndex).map { activityStartTime })
        return this
    }

    fun andCurrentTimeOf(time: SlackerTime): RunningFlowModelBuilder {
        currentTime = time
        return this
    }

    fun build(): RunningFlowModel {
        return RunningFlowModel(flow!!, deadline!!, log!!, currentTime!!)
    }

    fun andDeadlineOf(deadline: SlackerTime): RunningFlowModelBuilder {
        this.deadline = deadline
        return this
    }

    private var flow: Flow? = null
    private var deadline: SlackerTime? = null
    private var log: ActivitiesLog? = null
    private var currentTime: SlackerTime? = null
}
