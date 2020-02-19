package net.igorbrejc.timeslack

data class SlackerActivitiesLog(
    val planStartTime: SlackerTime,
    val activitiesFinishTimes: List<SlackerTime>
) {

    fun currentActivityIndex(): Int { return activitiesFinishTimes.count() }
    fun currentActivityStartTime(): SlackerTime {
        return when (currentActivityIndex()) {
            0 -> planStartTime
            else -> activitiesFinishTimes[currentActivityIndex()]
        }
    }
}
