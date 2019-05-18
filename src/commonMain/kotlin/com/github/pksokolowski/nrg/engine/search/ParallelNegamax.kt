package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import kotlinx.coroutines.*

class ParallelNegamax(val workersCount: Int) {
    fun launch(
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

        val queue = MovesQueue(possibleMoves)

        val jobs = GlobalScope.async {
            val workers = List(workersCount) {
                async(Dispatchers.Default) {
                    while (true) {
                        val move = queue.pickNext() ?: break

                        state.applyMove(move)
                        val score =
                            -negamax(state, depth - 1, 1, -MAX_SCORE, -bestScore, deadline, -player, tTable, killers)
                        state.undoMove(move)

                        if (score > bestScore) {
                            bestMove = possibleMoves[0] //todo change to something meaningful
                            bestScore = score
                        }
                    }
                }
            }

            workers.awaitAll()

            println(
                """
            best score = $bestScore
            best move = $bestMove
        """.trimIndent()
            )
        }

        return bestMove
    }

    private class MovesQueue(private val moves: List<Move>) {
        private var nextMoveIndex = 0
        fun pickNext() = moves.getOrNull(nextMoveIndex++)
    }
}