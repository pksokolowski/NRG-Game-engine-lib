package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.possibleMovesFrom
import com.github.pksokolowski.nrg.engine.search.pickBestMoveFrom
import com.github.pksokolowski.nrg.engine.utils.getInitialGameState

fun play(query: EngineQuery) = with(query) {
    state.applyMove(chosenMove)

    // todo consider better way to handle end game - include points and a flag?
    val bestMovInfo = pickBestMoveFrom(state, depthAllowed, timeAllowed, randomize)
    val bestMove = bestMovInfo.move ?: return EngineResponse(state, listOf(), 0)

    state.applyMove(bestMove)

    val possibleMoves = possibleMovesFrom(state)
    EngineResponse(state, possibleMoves, bestMovInfo.depthReached)
}

fun startGame(): EngineResponse {
    val state = getInitialGameState()
    val possibleMoves = possibleMovesFrom(state)
    return EngineResponse(state, possibleMoves, 0)
}