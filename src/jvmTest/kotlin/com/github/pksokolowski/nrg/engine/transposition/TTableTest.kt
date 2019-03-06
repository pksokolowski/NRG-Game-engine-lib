package com.github.pksokolowski.nrg.engine.transposition

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
class TTableTest {
    @Test
    fun `can retrieve table entry information`() {
        val state = """
            00 +1 +2 00
            00 00 00 00
            -1 00 -1 00
        """.toGameState()
        val table = TTable(state, 100)
        val move = Move(-1,1,1,1,0,0)

        val firstAccess = table[state]
        firstAccess.bestMove = move
        val secondAccess = table[state]

        assertEquals(move, secondAccess.bestMove)
    }
}