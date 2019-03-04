package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.getDeadline
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed

fun pickBestMoveFrom(state: GameState, depth: Int, timeLimit: Long? = null, randomize: Boolean = false): Move? {
    val deadline = getDeadline(timeLimit)
    var chosenMove: Move? = null

    val player = state.playerActive
    val possibleMoves = possibleMovesFromOrNull(state)
            ?: return null

    if(randomize) possibleMoves.shuffle()
    possibleMoves.orderMoves(player)

    for(i in 1..depth){
        val bestMove = pickBestMoveFullDepth(player, possibleMoves, state, i, deadline)
        if(isDeadlineCrossed(deadline)) break
        chosenMove = bestMove
    }
    return chosenMove
}

private fun pickBestMoveFullDepth(player: Int, possibleMoves: List<Move>, state: GameState, depth: Int, deadline: Long? = null): Move? {
    var bestMove = possibleMoves[0]
    var bestScore = Int.MIN_VALUE + 1

    for (move in possibleMoves) {
        state.applyMove(move)
        val score = -negamax(state, depth - 1, -Int.MAX_VALUE, -bestScore, deadline, -player)
        state.undoMove(move)

        if (score > bestScore) {
            bestMove = move
            bestScore = score
        }
    }

    return bestMove
}

// todo implement randomization, basically shuffle moves generated before ordering