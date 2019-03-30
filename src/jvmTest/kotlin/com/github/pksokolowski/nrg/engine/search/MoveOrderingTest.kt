package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move
import kotlin.test.Test
import kotlin.test.assertTrue

class MoveOrderingTest {
    @Test
    fun `orders captures first, for human player`() {
        val actual = mutableListOf(
            NEUTRAL,
            BIG_CAPTURE,
            CAPTURE
        ).orderMoves(-1)
        val expected = arrayOf(
            BIG_CAPTURE,
            CAPTURE,
            NEUTRAL
        )

        assertTrue(expected.contentDeepEquals(actual.toTypedArray()))
    }

    @Test
    fun `orders captures first, for AI player`() {
        val actual = mutableListOf(
            NEUTRAL,
            BIG_CAPTURE_HUMAN,
            CAPTURE_HUMAN
        ).orderMoves(1)
        val expected = arrayOf(
            BIG_CAPTURE_HUMAN,
            CAPTURE_HUMAN,
            NEUTRAL
        )

        assertTrue(expected.contentDeepEquals(actual.toTypedArray()))
    }

    @Test
    fun `self captures are on par with non-captures, the order of those two categories of moves doesn't alter`() {
        val actual = mutableListOf(
            CAPTURE_HUMAN,
            NEUTRAL,
            BIG_CAPTURE,
            CAPTURE
        ).orderMoves(-1)
        val expected = arrayOf(
            BIG_CAPTURE,
            CAPTURE,
            CAPTURE_HUMAN,
            NEUTRAL
        )

        assertTrue(expected.contentDeepEquals(actual.toTypedArray()))
    }

    @Test
    fun `places a provided 'best' move first`() {
        val actual = mutableListOf(
            NEUTRAL,
            BIG_CAPTURE,
            CAPTURE
        ).orderMoves(-1, NEUTRAL)
        val expected = arrayOf(
            NEUTRAL,
            BIG_CAPTURE,
            CAPTURE
        )

        assertTrue(expected.contentDeepEquals(actual.toTypedArray()))
    }

    companion object {
        val NEUTRAL = Move(-4, 1, 1, 2, 2, 0)
        val NEUTRAL_2 = Move(-3, 2, 2, 3, 3, 0)
        val CAPTURE = Move(-4, 1, 2, 2, 3, 1)
        val BIG_CAPTURE = Move(-4, 1, 2, 2, 3, 4)
        val CAPTURE_HUMAN = Move(4, 1, 2, 2, 3, -1)
        val BIG_CAPTURE_HUMAN = Move(4, 1, 2, 2, 3, -4)
    }
}