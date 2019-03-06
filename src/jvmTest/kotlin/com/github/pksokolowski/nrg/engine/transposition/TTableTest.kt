package com.github.pksokolowski.nrg.engine.transposition

import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.TTableEntry
import com.github.pksokolowski.nrg.engine.utils.toGameState
import org.junit.Test

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


        val entry = TTableEntry()
    }
}