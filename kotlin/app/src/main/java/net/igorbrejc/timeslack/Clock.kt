package net.igorbrejc.timeslack

interface Clock {
    fun now(): SlackerTime
}
