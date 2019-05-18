package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.ParallelNegamaxTest.Case.EARLY_GAME
import com.github.pksokolowski.nrg.engine.search.ParallelNegamaxTest.Case.LATE_GAME
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.ZobristHash
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.fail

class ParallelNegamaxTest {

    @Test
    fun `finds best move in simple scenario`() =
        givenHeavlilyParallelNegamax()
            .whenGameIsInEarlyState()
            .thenSearchReturnsBestMove()

    @Test
    fun `finds best move in late game scenario with 1 thread`() =
        givenSingleThreadedParallelNegamax()
            .whenGameIsInLateState()
            .thenSearchReturnsBestMove()

    @Test
    fun `finds best move in late game scenario with multiple threads`() =
        givenHeavlilyParallelNegamax()
            .whenGameIsInLateState()
            .thenSearchReturnsBestMove()

    @Test
    fun `returns same move as sequential implementation in late game scenario`() =
        givenHeavlilyParallelNegamax()
            .whenGameIsInLateState()
            .thenReturnSameMoveAsSequentialImplementation()


    private fun givenSingleThreadedParallelNegamax() = ParallelNegamax(1)
    private fun givenHeavlilyParallelNegamax() = ParallelNegamax(5)

    private fun ParallelNegamax.whenGameIsInEarlyState() = TestData(this, EARLY_GAME)
    private fun ParallelNegamax.whenGameIsInLateState() = TestData(this, LATE_GAME)

    private class TestData(val parallelNegamax: ParallelNegamax, val case: Case)

    private enum class Case(val state: GameState, val searchDepth: Int, val bestMove: Move) {
        EARLY_GAME(
            """
        +2 00 00 00 +1 +2
        00 +1 +3 +2 +2 00
        00 00 00 00 00 00
        00 00 00 00 -3 -4
        00 -2 -3 -1 -1 -1
        00 00 00 00 00 -1
    """.toGameState(), 3, Move(-3, 2, 4, 2, 1, 3)
        ),
        LATE_GAME(
            """
        +2 +2 00 00 00 00
        00 00 00 00 00 00
        00 00 00 +4 00 00
        00 00 00 -3 00 00
        00 -1 00 00 00 00
        00 -1 00 00 00 00
    """.toGameState(1), 3, Move(4, 3, 2, 3, 3, -3)
        )
    }

    private fun TestData.thenSearchReturnsBestMove() {
        val params = NegamaxParams(case.state, case.searchDepth)
        val result = runParallelNegamaxWithParams(params)
        if (result != case.bestMove) fail("best move wasn't found\ngot      $result\nexpected ${case.bestMove}")
    }

    private class NegamaxParams(
        val state: GameState,
        val depth: Int
    ) {
        val possibleMoves = possibleMovesFrom(state)
        val player = state.playerActive
        val tt = getTranspositionTable(state)
        val killers = KillerHeuristic(depth)

        private fun getTranspositionTable(state: GameState): TTable {
            val hashMaker = ZobristHash(state.width, state.height)
            return TTable(hashMaker, 1)
        }
    }

    private fun runNegamaxWithParams(params: NegamaxParams) = params.run {
        -negamax(state, depth, 1, -MAX_SCORE, -MIN_SCORE, null, -player, tt, killers)
    }

    private fun runSequentialImplementationWithParams(params: NegamaxParams) = params.run {
        pickBestMoveSequentially(possibleMoves, state, depth, null, tt, killers)
    }

    private fun runParallelNegamaxWithParams(params: NegamaxParams) = params.run {
        val parallelNegamax = ParallelNegamax(3)
        parallelNegamax.launch(possibleMoves, state, depth, null, tt, killers)
    }

    private fun TestData.thenReturnSameMoveAsSequentialImplementation() {
        val params = NegamaxParams(case.state, case.searchDepth)
        val result = runParallelNegamaxWithParams(params)
        val expected = runSequentialImplementationWithParams(params)
        if (result != expected) fail("best move wasn't found\ngot      $result\nexpected $expected")
    }

    /**
     * An old, sequential implementation for comparison purposes
     */
    private fun pickBestMoveSequentially(
        possibleMoves: List<Move>,
        state: GameState,
        depth: Int,
        deadline: Long? = null,
        tTable: TTable,
        killers: KillerHeuristic
    ): Move {
        val player = state.playerActive
        var bestMove = possibleMoves[0]
        var bestScore = MIN_SCORE

        for (move in possibleMoves) {
            state.applyMove(move)
            val score = -negamax(state, depth - 1, 1, -MAX_SCORE, -bestScore, deadline, -player, tTable, killers)
            state.undoMove(move)

            if (score > bestScore) {
                bestMove = move
                bestScore = score
            }
        }

        return bestMove
    }
}