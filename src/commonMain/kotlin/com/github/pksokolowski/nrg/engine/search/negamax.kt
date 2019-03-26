package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.search.transposition.NodeType
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed
import kotlin.math.max
import kotlin.math.min

fun negamax(state: GameState, depthLeft: Int, alpha: Int, beta: Int, deadline: Long?, player: Int, tTable: TTable): Int {
    if (isDeadlineCrossed(deadline)) return MIN_SCORE

    if (depthLeft == 0) return state.evaluateForActivePlayer()
    var newA = alpha
    var newB = beta

    val ttEntry = tTable[state]
    if (ttEntry.depth >= depthLeft) {
        when (ttEntry.type) {
            NodeType.EXACT -> return ttEntry.bestScore
            NodeType.LOWER -> newA = max(alpha, ttEntry.bestScore)
            NodeType.UPPER -> newB = min(beta, ttEntry.bestScore)
        }
        if(newA >= newB) return ttEntry.bestScore
    }

    val moves = possibleMovesFromOrNull(state)?.orderMoves(player, ttEntry.bestMove)
        ?: return state.evaluateForActivePlayer()

    var bestScore = Int.MIN_VALUE + 1
    var bestMove = moves[0]
    for (it in moves) {
        state.applyMove(it)
        val score = -negamax(state, depthLeft - 1, -newB, -newA, deadline, -player, tTable)
        if (score > bestScore) {
            bestScore = score
            bestMove = it
        }
        state.undoMove(it)
        newA = max(bestScore, newA)
        if (newA >= newB) break
    }

    ttEntry.update(bestScore, alpha, beta, bestMove, depthLeft)

    return bestScore
}