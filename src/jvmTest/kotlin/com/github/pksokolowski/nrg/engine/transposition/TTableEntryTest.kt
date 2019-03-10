package com.github.pksokolowski.nrg.engine.transposition

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.NodeType
import com.github.pksokolowski.nrg.engine.search.transposition.TTableEntry
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class TTableEntryTest {
    @Test
    fun `retrieves all stored information`() {
        val entry = TTableEntry(1UL)

        val bestScore = 1
        val depth = 4
        val move = Move(4, 2, 3, 4, 5, 1)
        val type = NodeType.LOWER

        entry.update(bestScore, 3, 3, move, depth)

        if (entry.bestScore != bestScore
            || entry.bestMove != move
            || entry.type != type
        ) fail()
    }

    @Test
    fun `assigns LOWER node type when best score is below alpha`() {
        val entry = TTableEntry(1UL)
        val move = Move(1, 1, 1, 1, 0, 0)
        entry.update(1, 2, 3, move, 2)

        assertEquals(NodeType.LOWER, entry.type)
    }

    @Test
    fun `assigns UPPER node type when best score is above beta (cutoff case - too good to be picked, the other player won't allow this path)`() {
        val entry = TTableEntry(1UL)
        val move = Move(1, 1, 1, 1, 0, 0)
        entry.update(4, 4, 3, move, 2)

        assertEquals(NodeType.UPPER, entry.type)
    }
}