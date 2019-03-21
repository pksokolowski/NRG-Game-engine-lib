package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState

fun evaluate(state: GameState): Int {
    var sum = 0
    state.forAllSquares { x, y ->
        sum += state[x, y]
    }
    return sum
}

fun evaluateForActivePlayer(state: GameState) = evaluate(state) * state.playerActive