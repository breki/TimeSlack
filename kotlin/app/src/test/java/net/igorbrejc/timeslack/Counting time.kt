package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class `Counting time` {
    @Test
    fun `SlackerTime fails if hours is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerTime.of(-1, 33) }
    }

    @Test
    fun `SlackerTime fails if hours is greater than 23`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerTime.of(24, 33) }
    }

    @Test
    fun `SlackerTime fails if minutes is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerTime.of(10, -1) }
    }

    @Test
    fun `SlackerTime fails if minutes is greater than 59`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerTime.of(10, 60) }
    }

    @Test
    fun `SlackerTime returns correct hours and minutes of day`() {
        val time = SlackerTime.of(10, 44)
        assertEquals(10, time.hours)
        assertEquals(44, time.minutes)
    }

    @Test
    fun `SlackerDuration fails if minutes are negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerDuration(-1)
        }
    }

    @Test
    fun `SlackerDuration fails if minutes are zero`() {
        assertThrows(IllegalArgumentException::class.java) {
            SlackerDuration(0)
        }
    }
}