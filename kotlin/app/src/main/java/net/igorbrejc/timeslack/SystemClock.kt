package net.igorbrejc.timeslack

class SystemClock : Clock {
    override fun now(): SlackerTime {
        return SlackerTime.now()
    }
}
