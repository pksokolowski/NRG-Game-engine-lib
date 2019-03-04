package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals


class StateGeneratorTest {
    @Test
    fun `generates basic state`() {
        val state = """
            00 00 00
            -1 00 00
            00 +3 00
        """.toGameState()

        assertEquals(-1, state[0, 1])
        assertEquals(3, state[1, 2])
    }

    @Test
    fun `parses all corners correctly`() {
        val state = """
            +3 00 +4
            00 00 00
            +1 00 +2
        """.toGameState()

        assertEquals(1, state[0, 2])
        assertEquals(2, state[2, 2])
        assertEquals(3, state[0, 0])
        assertEquals(4, state[2, 0])
    }

    @Test
    fun `parses a cross correctly`() {
        val state = """
            00 00 +1 00 00
            00 00 +2 00 00
            +1 +2 +3 +4 +5
            00 00 +4 00 00
            00 00 +5 00 00
        """.toGameState()

        // middle point
        assertEquals(3, state[2, 2])
        // horizontal line
        assertEquals(1, state[0, 2])
        assertEquals(2, state[1, 2])
        assertEquals(4, state[3, 2])
        assertEquals(5, state[4, 2])
        // vertical line
        assertEquals(1, state[2, 0])
        assertEquals(2, state[2, 1])
        assertEquals(4, state[2, 3])
        assertEquals(5, state[2, 4])
    }

    @Test
    fun `generates more complex state`() {
        val state = """
            00 00 00 -4
            -1 00 00 00
            +2 +3 +1 +1
            +1 +2 +3 +4
        """.toGameState()

        // row 1
        assertEquals(-4, state[3, 0])
        // row 2
        assertEquals(-1, state[0, 1])
        // row 3
        assertEquals(2, state[0, 2])
        assertEquals(3, state[1, 2])
        assertEquals(1, state[2, 2])
        assertEquals(1, state[3, 2])
        // row 4
        assertEquals(1, state[0, 3])
        assertEquals(2, state[1, 3])
        assertEquals(3, state[2, 3])
        assertEquals(4, state[3, 3])
    }

    @Test
    fun `can generate a vertical, non-square board`() {
        val state = """
            -4
            +1
            00
        """.toGameState(1)
    }

    @Test
    fun `can generate a horizontal, non-square board`() {
        val state = """
            +1 +2 -3
        """.toGameState(1)
    }

    @Test
    fun `can generate a 2 by 4 non-square board`() {
        val state = """
            -4 00
            +1 +2
            00 -1
            -2 -3
        """.toGameState(1)

        assertEquals(-2, state[0, 3])
        assertEquals(-3, state[1, 3])
        assertEquals(0, state[0, 2])
        assertEquals(-1, state[1, 2])
        assertEquals(1, state[0, 1])
        assertEquals(2, state[1, 1])
        assertEquals(-4, state[0, 0])
        assertEquals(0, state[1, 0])
    }
}