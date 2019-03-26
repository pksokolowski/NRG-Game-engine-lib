package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE

fun evaluate(state: GameState): Int {
    var sumNegative = 0
    var sumPositive = 0
    state.forAllSquares { x, y ->
        state[x, y].let {
            when {
                it < 0 -> sumNegative += it
                it > 0 -> sumPositive += it
            }
        }
    }
    if (sumPositive == 0) return MIN_SCORE + state.movesCount
    if (sumNegative == 0) return MAX_SCORE - state.movesCount
    return sumPositive + sumNegative
}

fun evaluateForActivePlayer(state: GameState): Int {
    return state.getEvaluation() * state.playerActive
}

fun evaluatePlayerMateriel(state: GameState, player: Int): Int {
    var sum = 0
    state.forAllSquares iteration@{ x, y ->
        val square = state[x, y]
        if (square == 0) return@iteration
        if ((square > 0) xor (player > 0)) return@iteration

        sum += square
    }
    return sum
}