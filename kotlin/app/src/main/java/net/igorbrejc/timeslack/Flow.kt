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

    fun startFlow(startTime: SlackerTime): FlowLog {
        val steps = activities.map {
            activity ->
                when (activity) {
                    is FixedActivity -> {
                        ImmutableList.of (
                            FixedActivityStep(activity.activityName))
                    }

                    is DynamicActivity -> {
                        ImmutableList.of (
                            DynamicActivityForwardStep(activity.activityName),
                            DynamicActivityReturnStep(
                                activity.activityName + " (return)"))
                    }
                }
        }

        return FlowLog(steps.flatten())
    }
}
