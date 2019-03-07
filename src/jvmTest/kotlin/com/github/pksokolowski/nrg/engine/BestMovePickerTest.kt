package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.pickBestMoveFrom
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals

class BestMovePickerTest {
    @Test
    fun `picks the best move in an obvious case`() {
        val state = """
            00 +1 +1
            -1 00 -4
            00 00 00
        """.toGameState(1)

        val bestMove = Move(1, 1, 0, 2, 1, -4)
        val pickedMove = pickBestMoveFrom(state, 8)

        assertEquals(bestMove, pickedMove.move)
    }

    @Test
    fun `picks the best move in an obvious case for human player`() {
        val state = """
            00 00 00
            +4 00 +1
            -1 -1 00
        """.toGameState(0)

        val bestMove = Move(-1, 1, 2, 0, 1, 4)
        val pickedMove = pickBestMoveFrom(state, 8)

        assertEquals(bestMove, pickedMove.move)
    }

    @Test
    fun `picks the only good (winning) move possible when presented with immediate loss otherwise`() {
        val state = """
            00 +1 00
            00 +1 +1
            00 00 -4
        """.toGameState(0)

        val bestMove = Move(-4, 2, 2, 1, 1, 1)
        val pickedMove = pickBestMoveFrom(state, 8)

        assertEquals(bestMove, pickedMove.move)
    }
}