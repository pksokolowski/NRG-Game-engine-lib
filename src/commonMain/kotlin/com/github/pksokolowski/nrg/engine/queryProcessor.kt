package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.Status.*
import com.github.pksokolowski.nrg.engine.Status.FINISHED.Player.*
import com.github.pksokolowski.nrg.engine.search.possibleMovesFrom
import com.github.pksokolowski.nrg.engine.search.pickBestMoveFrom
import com.github.pksokolowski.nrg.engine.utils.getInitialGameState

fun play(query: EngineQuery) = with(query) {
    state.applyMove(chosenMove)

    val bestMoveData = pickBestMoveFrom(state, depthAllowed, timeAllowed, randomize).apply {
        move?.let { state.applyMove(it) }
    }

    val possibleMoves = possibleMovesFrom(state)
    EngineResponse(state, possibleMoves, bestMoveData.depthReached, state.getStatus(possibleMoves))
}

fun startGame(): EngineResponse {
    val state = getInitialGameState()
    val possibleMoves = possibleMovesFrom(state)
    return EngineResponse(state, possibleMoves, 0, ONGOING)
}

private fun GameState.getStatus(possibleMoves: List<Move>) = if (possibleMoves.isEmpty()) {
    getEndGameStatus(this)
} else {
    ONGOING
}

/**
 * returns game result from the human player's perspective. Hence -evaluate()...
 */
private fun getEndGameStatus(state: GameState) = (-state.evaluate()).let {
    val winner = when {
        it > 0 -> HUMAN
        it < 0 -> AI
        else -> NOBODY
    }
    FINISHED(winner, it)
}
