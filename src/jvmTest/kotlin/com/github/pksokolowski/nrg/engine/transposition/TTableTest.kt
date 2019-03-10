package com.github.pksokolowski.nrg.engine.transposition

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.HashMaker
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.ZobristHash
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@ExperimentalUnsignedTypes
class TTableTest {
    @Test
    fun `can retrieve table entry information`() {
        val state = """
            00 +1 +2 00
            00 00 00 00
            -1 00 -1 00
        """.toGameState()
        val hashMaker = ZobristHash(state.width, state.height)
        val table = TTable(hashMaker, 1)
        val move = Move(-1, 1, 1, 1, 0, 0)

        val firstAccess = table[state]
        firstAccess.update(0, 0,1, move, 9)
        val secondAccess = table[state]

        assertEquals(move, secondAccess.bestMove)
    }

    @Test
    fun `can detect index collision, with no hash collision`() {
        val state = """
            00 +1 +2 00
            00 00 00 00
            -1 00 -1 00
        """.toGameState()
        val state2 = """
            00 +1 +2 00
            00 00 00 00
            -1 00 -1 00
        """.toGameState(1)
        val hashMaker = ZobristHash(state.width, state.height)
        val table = TTable(hashMaker, 1)
        val move = Move(-1, 1, 1, 1, 0, 0)

        val firstAccess = table[state]
        firstAccess.update(0, 0,1, move, 9)
        val secondAccess = table[state2]

        assertEquals(null, secondAccess.bestMove)
    }

    @Test
    fun `holds multiple entries simultaneously`() {
        val states = listOf(
            """
            00 00 00
            00 -1 00
            00 00 00
        """.toGameState(),
            """
            00 00 00
            00 +1 00
            00 00 00
        """.toGameState(),
            """
            00 00 00
            00 -3 00
            00 00 +2
        """.toGameState(),
            """
            +1 +1 +1
            00 00 00
            -1 -1 -1
        """.toGameState()
        )

        //val hashMaker = ZobristHash(9, 9)
        val hashMaker = NineSquarePredictableHashMaker()
        val table = TTable(hashMaker, 74)

        for ((i, state) in states.withIndex()) {
            val entry = table[state]
            if (entry.bestMove != null) fail()

            val move = Move(0,0,0,0,0,0)
            entry.update(i, 0,i+2, move, 0)
        }

        for (state in states) {
            val entry = table[state]
            if (entry.bestMove == null) fail()
        }
    }

    /**
     * This hashMaker doesn't differentiate between player 1 and player -1 turns
     */
    private class NineSquarePredictableHashMaker : HashMaker {
        override fun hashOf(gameState: GameState): ULong {
            require(gameState.width * gameState.height <= 9) { "NineSquarePredictableHashMaker cannot operate on as large a board without lifting the guarantee of no collisions" }
            var decimalPlace = 0
            var hash = 0UL
            for (x in 0 until gameState.width) for (y in 0 until gameState.height) {
                val piecePositive = gameState[x, y] + MAX_ENERGY + 1
                val relativeZero = 10f.pow(decimalPlace).toInt()
                hash += (relativeZero * piecePositive).toULong()
                decimalPlace += 2
            }
            return hash
        }
    }
}