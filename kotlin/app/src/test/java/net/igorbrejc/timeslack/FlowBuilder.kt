package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

class FlowBuilder {
    fun withSampleActivities() : FlowBuilder {
        activities.addAll(
            ImmutableList.of(
                FixedActivity("prepare things", SlackerDuration(10)),
                FixedActivity("drive", SlackerDuration(15)),
                FixedActivity("prepare for hiking", SlackerDuration(5)),
                FixedActivity("hiking", SlackerDuration(60)),
                FixedActivity("unpack", SlackerDuration(5)),
                FixedActivity("drive", SlackerDuration(15)),
                FixedActivity("wash & dress", SlackerDuration(15))
            )
        )

        return this
    }

    fun addActivity(activity: FlowActivity) : FlowBuilder {
        activities.add(activity)
        return this
    }

    fun build() : Flow = Flow(activities)

    private val activities: MutableList<FlowActivity> = mutableListOf()
}