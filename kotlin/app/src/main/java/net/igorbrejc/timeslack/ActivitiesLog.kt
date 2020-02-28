package net.igorbrejc.timeslack

import com.google.common.collect.ImmutableList

data class ActivitiesLog(
    val activitiesCount: Int,
    val timePoints: List<SlackerTime>
) {
    init {
        when {
            timePoints.count() == 0 -> throw IllegalArgumentException(
                "At least one time point is required, to specify the start " +
                "time of the first activity.")

            timePoints.count() > activitiesCount + 1 ->
                throw IllegalArgumentException(
                "Too many time points for the provided number of activities.")
        }
    }

    fun currentActivityIndex(): Int { return timePoints.count() - 1 }

    fun currentActivityStartTime(): SlackerTime =
        timePoints[currentActivityIndex()]

    fun finishActivity(currentTime: SlackerTime): ActivitiesLog {
        return ActivitiesLog(
            activitiesCount,
            ImmutableList.builder<SlackerTime>()
                .addAll(timePoints)
                .add(currentTime)
                .build())
    }
}
