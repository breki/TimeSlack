package net.igorbrejc.timeslack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Formatting time` {
    @Test
    fun `renders time without leading zeros if not needed`() {
        assertEquals(
            "13:44",
            SlackerTime.of(13, 44).toString())
    }

    @Test
    fun `adds leading zero to minutes only`() {
        assertEquals(
            "9:09",
            SlackerTime.of(9, 9).toString())
    }
}