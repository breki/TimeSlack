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
                        ImmutableList.of(
                            FixedActivityStep(activity.activityName))
                    }

                    is DynamicActivity -> {
                        ImmutableList.of(
                            DynamicActivityForwardStep(activity.activityName),
                            DynamicActivityReturnStep(
                                activity.activityName + " (return)"))
                    }
                }
        }

        // TODO: test for asserting the startTime was used here
        return FlowLog(steps.flatten(), ImmutableList.of(SlackerTime.of(1, 1)))
    }
}
