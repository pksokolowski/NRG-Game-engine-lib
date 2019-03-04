package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.possibleMovesFrom
import com.github.pksokolowski.nrg.engine.utils.makeMatrix
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SuccessorFunctionTest {
    @Test
    fun `generates possible moves and no others`() {
        val state = """
            +1 00 00 +1
            00 00 00 00
            00 00 00 00
            00 00 00 00
        """.toGameState(1)

        val possibleMoves = listOf(
            Move(1, 0, 0, 0, 1, 0),
            Move(1, 3, 0, 3, 1, 0)
        )

        assertTrue(possibleMovesFrom(state).containsAll(possibleMoves))
    }

    @Test
    fun `generates advanced moves for a high energy piece`() {
        val state = """
            00 00 00 00 00
            00 00 00 00 00
            00 00 -4 00 00
            00 00 00 00 00
            00 00 00 00 00
        """.toGameState()

        val possibleMoves = listOf(
            // up, down, left, right; 2 in each direction
            Move(-4, 2, 2, 2, 1, 0),
            Move(-4, 2, 2, 2, 0, 0),
            Move(-4, 2, 2, 2, 3, 0),
            Move(-4, 2, 2, 2, 4, 0),
            Move(-4, 2, 2, 3, 2, 0),
            Move(-4, 2, 2, 4, 2, 0),
            Move(-4, 2, 2, 1, 2, 0),
            Move(-4, 2, 2, 0, 2, 0),

            // diagonals 2 in each direction
            Move(-4, 2, 2, 3, 1, 0),
            Move(-4, 2, 2, 4, 0, 0),
            Move(-4, 2, 2, 1, 1, 0),
            Move(-4, 2, 2, 0, 0, 0),
            Move(-4, 2, 2, 3, 3, 0),
            Move(-4, 2, 2, 4, 4, 0),
            Move(-4, 2, 2, 1, 3, 0),
            Move(-4, 2, 2, 0, 4, 0)
        )

        assertTrue(possibleMovesFrom(state).containsAll(possibleMoves))
    }

    @Test
    fun `respects boundaries of the board`() {
        val state = """
            00 00 00
            00 00 00
            00 00 -4
        """.toGameState(0)

        val possibleMoves = listOf(
            // up and left (both reversed) 2 in each direction
            Move(-4, 2, 2, 2, 1, 0),
            Move(-4, 2, 2, 2, 0, 0),
            Move(-4, 2, 2, 1, 2, 0),
            Move(-4, 2, 2, 0, 2, 0),

            // diagonals 2 in each direction
            Move(-4, 2, 2, 1, 1, 0),
            Move(-4, 2, 2, 2, 0, 0)
        )
        val generatedMoves = possibleMovesFrom(state)
        assertTrue(generatedMoves.containsAll(possibleMoves))
    }

    @Test
    fun `handles collision on NON_CAPTURE mobility`() {
        val matrix = makeMatrix(8, 8).apply {
            this[2][1] = 1
            this[2][2] = 1
        }
        val state = GameState(matrix, 1)
        val possibleMoves = listOf(
            Move(1, 2, 2, 2, 3, 0)
        )

        assertTrue(possibleMoves.containsAll(possibleMovesFrom(state)))
    }

    @Test
    fun `only generates moves for the player whose turn it is`() {
        val matrix = makeMatrix(8, 8).apply {
            this[2][0] = 1
            this[2][7] = -1
        }
        val state = GameState(matrix, 2)
        val possibleMoves = listOf(
            Move(-1, 2, 7, 2, 6, 0)
        )

        assertTrue(possibleMoves.containsAll(possibleMovesFrom(state)))
    }

    @Test
    fun `generates captures for energy 1 pieces`() {
        val matrix = makeMatrix(8, 8).apply {
            this[3][3] = 1
            this[4][4] = -1
            this[2][4] = -2
        }
        val state = GameState(matrix, 1)
        val possibleMoves = listOf(
            Move(1, 3, 3, 3, 4, 0),
            Move(1, 3, 3, 2, 4, -2),
            Move(1, 3, 3, 4, 4, -1)
        )

        assertTrue(possibleMoves.containsAll(possibleMovesFrom(state)))
    }

    @Test
    fun `supports high energy pieces`() {
        val matrix = makeMatrix(8, 8).apply {
            this[0][0] = 3
        }
        val state = GameState(matrix, 1)
        val possibleMoves = listOf(
            Move(3, 0, 0, 0, 1, 0),
            Move(3, 0, 0, 0, 2, 0),
            Move(3, 0, 0, 1, 0, 0),
            Move(3, 0, 0, 2, 0, 0)
        )

        assertTrue(possibleMoves.containsAll(possibleMovesFrom(state)))
    }

    @Test
    fun `one piece cannot jump over another, has to collide`() {
        val state = """
            00 00 -4
            00 +1 +1
            00 +1 00
        """.toGameState(2)

        val possibleMoves = possibleMovesFrom(state)

        assertFalse(possibleMoves.contains(Move(-4, 2, 2, 2, 0, 0)))
    }

    @Test
    fun `can't capture forwards with a energy 1 piece`() {
        val state = """
            00 +1 00
            +1 -5 00
            00 00 00
        """.toGameState(1)

        val possibleMoves = possibleMovesFrom(state)

        assertFalse(possibleMoves.contains(Move(1, 1, 0, 1, 1, -5)))
    }
}