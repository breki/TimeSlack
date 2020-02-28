package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

data class ActivitiesLog(val timePoints: List<SlackerTime>) {
    fun currentActivityIndex(): Int { return timePoints.count() - 1 }

    fun currentActivityStartTime(): SlackerTime =
        timePoints[currentActivityIndex()]

    fun finishActivity(currentTime: SlackerTime): ActivitiesLog {
        return ActivitiesLog(
            ImmutableList.builder<SlackerTime>()
                .addAll(timePoints)
                .add(currentTime)
                .build())
    }
}
