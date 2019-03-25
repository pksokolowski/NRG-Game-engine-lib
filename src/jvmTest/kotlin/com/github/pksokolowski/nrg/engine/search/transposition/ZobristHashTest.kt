package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalUnsignedTypes
class ZobristHashTest {
    @Test
    fun `hash is 0 when board is empty and human player is to move`() {
        val hashMaker = ZobristHash(8, 8)
        val state = """
            00 00 00
            00 00 00
            00 00 00
        """.toGameState()

        val hash = hashMaker.hashOf(state)
        assertEquals(0UL, hash)
    }

    @Test
    fun `hash is not zero when it's AI's move`() {
        val hashMaker = ZobristHash(8, 8)
        val state = """
            00 00 00
            00 00 00
            00 00 00
        """.toGameState(1)

        val hash = hashMaker.hashOf(state)
        assertNotEquals(0UL, hash)
    }
}