package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

data class Flow(
    val activities: List<FlowActivity>
) {
    fun createActivitiesLog(flowStartTime: SlackerTime): ActivitiesLog {
        return ActivitiesLog(
            activities.count(),
            ImmutableList.of(flowStartTime))
    }
}
