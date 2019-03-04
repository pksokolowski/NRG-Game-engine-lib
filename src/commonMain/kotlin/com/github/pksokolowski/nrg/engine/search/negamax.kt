package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed
import kotlin.math.max

fun negamax(state: GameState, depthLeft: Int, a: Int, b: Int, deadline: Long?, player: Int): Int {
    if (isDeadlineCrossed(deadline)) return Int.MIN_VALUE + 1
    fun evaluate() = evaluateForActivePlayer(state)
    if (depthLeft == 0) return evaluate()
    val moves = possibleMovesFromOrNull(state)?.orderMoves(player)
            ?: return evaluate()

    var newA = a
    var score = Int.MIN_VALUE + 1
    for (it in moves) {
        state.applyMove(it)
        score = max(score, -negamax(state, depthLeft - 1, -b, -newA, deadline, -player))
        state.undoMove(it)
        newA = max(score, newA)
        if (newA >= b) break
    }

    return score
}

// todo implement time limitation