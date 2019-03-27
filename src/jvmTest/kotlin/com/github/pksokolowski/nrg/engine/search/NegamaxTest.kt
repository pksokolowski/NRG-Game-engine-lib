package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.ZobristHash
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.makeMatrix
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NegamaxTest {
    @Test
    fun `basic case works`() {
        val matrix = makeMatrix(3, 3).apply {
            this[0][1] = -1
            this[1][0] = 3
        }
        val state = GameState(matrix, 0)
        val score = negamax(state, 1)
        assertEquals(MAX_SCORE - state.movesCount, score)
    }

    @Test
    fun `finds inevitable victory`() {
        val state = """
            +2 +3 +2
            00 00 00
            -1 -1 -1
        """.toGameState(1)
        val score = negamax(state, 9)
        assertTrue(score > 0)
    }

    @Test
    fun `returns score of (MAX_SCORE - total moves to victory (4) ) value when plus player takes all into one piece`() {
        val state = """
            +1 +2
            -1 -1
        """.toGameState(1)
        val score = negamax(state, 3)
        assertEquals(MAX_SCORE - 4, score)
    }

    private fun negamax(state: GameState, depth: Int, timeLimit: Long = Long.MAX_VALUE): Int {
        val player = state.playerActive
        val a = Int.MIN_VALUE + 1
        val b = Int.MAX_VALUE
        val hashMaker = ZobristHash(state.width, state.height)
        val tTable = TTable(hashMaker, 1)
        val killers = KillerHeuristic(depth)
        return negamax(state, depth, depth, a, b, timeLimit, player, tTable, killers)
    }
}