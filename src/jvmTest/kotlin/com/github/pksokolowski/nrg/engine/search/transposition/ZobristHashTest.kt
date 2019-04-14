package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.toGameState
import org.junit.runner.RunWith
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
//
//    @Test
//    fun `incremental hash is the same as normal in the beginning`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 00
//            00 -1 00
//            00 +1 -1
//        """.toGameState(1)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.hash
//        assertEquals(hash, hashIncremental)
//    }
//
//    @Test
//    fun `incremental hash is the same as normal after 1 move`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 00
//            00 -1 00
//            00 +1 -1
//        """.toGameState(1)
//
//        val move = Move(-1,1,1,1, 0, 0)
//        state.applyMove(move)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.hash
//        assertEquals(hash, hashIncremental)
//    }
//    @Test
//    fun `incremental hash is the same as normal after application and undoing a move`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 00
//            00 -1 00
//            00 +1 -1
//        """.toGameState(0)
//
//        val move = Move(-1,1,1,1, 0, 0)
//        state.applyMove(move)
//        state.undoMove(move)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.hash
//        assertEquals(hash, hashIncremental)
//    }
//
//    @Test
//    fun `incremental hash is the same as normal after application of 2 moves`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 +1
//            00 -1 00
//            00 +1 -1
//        """.toGameState(0)
//
//        val move = Move(-1,1,1,1, 0, 0)
//        state.applyMove(move)
//        val move2 = Move(1, 2, 0, 2, 1, 0)
//        state.applyMove(move2)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.hash
//        assertEquals(hash, hashIncremental)
//    }
//
//    @Test
//    fun `incremental hash is the same as normal after application of 2 moves and undoing both then making a capture move and undoing it`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 +1
//            00 -1 00
//            00 +1 -1
//        """.toGameState(0)
//
//        val move = Move(-1,1,1,1, 0, 0)
//        state.applyMove(move)
//        val move2 = Move(1, 2, 0, 2, 1, 0)
//        state.applyMove(move2)
//        // undo both moves
//        state.undoMove(move2)
//        state.undoMove(move)
//        // capture move
//        val move3 = Move(-1, 1,1,2,0, 1)
//        state.applyMove(move3)
//        state.undoMove(move3)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.hash
//        assertEquals(hash, hashIncremental)
//    }
//
//    @Test
//    fun `incremental hash is retained through copying a gameState`() {
//        val hashMaker = ZobristHash(8, 8)
//        val state = """
//            00 00 00
//            00 -1 00
//            00 +1 -1
//        """.toGameState(0)
//
//        val move = Move(-1,1,1,1, 0, 0)
//        state.applyMove(move)
//        state.undoMove(move)
//
//        val hash = hashMaker.hashOf(state)
//        val hashIncremental = state.copy().hash
//        assertEquals(hash, hashIncremental)
//    }
}