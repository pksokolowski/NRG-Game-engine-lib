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

fun evaluateForActivePlayer(state: GameState): Int{
    //if(evaluate(state) != state.getEvaluation()) throw RuntimeException("fsfsef")
    return  state.getEvaluation() * state.playerActive
}

fun evalPositives(state: GameState): Int {
    var sumPositive = 0
    state.forAllSquares { x, y ->
        state[x, y].let {
            when {
                it > 0 -> sumPositive += it
            }
        }
    }
   return sumPositive
}

fun evalNegatives(state: GameState): Int {
    var sumNegative = 0
    state.forAllSquares { x, y ->
        state[x, y].let {
            when {
                it < 0 -> sumNegative += it
            }
        }
    }
    return sumNegative
}