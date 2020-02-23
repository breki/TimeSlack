package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

data class SlackerActivitiesLog(
    val planStartTime: SlackerTime,
    val activitiesFinishTimes: List<SlackerTime>
) {
    fun currentActivityIndex(): Int { return activitiesFinishTimes.count() }

    fun currentActivityStartTime(): SlackerTime {
        return when (currentActivityIndex()) {
            0 -> planStartTime
            else -> activitiesFinishTimes[currentActivityIndex() - 1]
        }
    }

    fun finishActivity(currentTime: SlackerTime): SlackerActivitiesLog {
        return SlackerActivitiesLog(
            planStartTime,
            ImmutableList.builder<SlackerTime>()
                .addAll(activitiesFinishTimes)
                .add(currentTime)
                .build())
    }
}
