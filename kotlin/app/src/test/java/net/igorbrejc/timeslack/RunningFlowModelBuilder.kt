package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

class RunningFlowModelBuilder {
    fun givenAFlow(): RunningFlowModelBuilder {
        flow = Flow(
            ImmutableList.of(
                FlowActivity("prepare things", SlackerDuration(10)),
                FlowActivity("drive", SlackerDuration(15)),
                FlowActivity("prepare for hiking", SlackerDuration(5)),
                FlowActivity("hiking", SlackerDuration(60)),
                FlowActivity("unpack", SlackerDuration(5)),
                FlowActivity("drive", SlackerDuration(15)),
                FlowActivity("wash & dress", SlackerDuration(15))
            ))

        return this
    }

    fun andCurrentActivity(activityIndex: Int, activityStartTime: SlackerTime):
            RunningFlowModelBuilder {
        log = ActivitiesLog((1..activityIndex).map { activityStartTime })
        return this
    }

    fun andCurrentTimeOf(hours: Int, minutes: Int): RunningFlowModelBuilder {
        currentTime = SlackerTime.of(hours, minutes)
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
