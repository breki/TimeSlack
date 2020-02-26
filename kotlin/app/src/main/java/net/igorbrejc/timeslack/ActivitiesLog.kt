package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

data class ActivitiesLog(
    val flowStartTime: SlackerTime,
    val activitiesFinishTimes: List<SlackerTime>
) {
    fun currentActivityIndex(): Int { return activitiesFinishTimes.count() }

    fun currentActivityStartTime(): SlackerTime {
        return when (currentActivityIndex()) {
            0 -> flowStartTime
            else -> activitiesFinishTimes[currentActivityIndex() - 1]
        }
    }

    fun finishActivity(currentTime: SlackerTime): ActivitiesLog {
        return ActivitiesLog(
            flowStartTime,
            ImmutableList.builder<SlackerTime>()
                .addAll(activitiesFinishTimes)
                .add(currentTime)
                .build())
    }
}
