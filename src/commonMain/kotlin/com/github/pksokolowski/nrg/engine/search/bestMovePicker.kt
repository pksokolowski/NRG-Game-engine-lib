package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.ZobristHash
import com.github.pksokolowski.nrg.engine.utils.getDeadline
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed

class BestMoveData(val move: Move?, val depthReached: Int)

fun pickBestMoveFrom(state: GameState, depth: Int, timeLimit: Long? = null, randomize: Boolean = false): BestMoveData {
    val deadline = getDeadline(timeLimit)
    var chosenMove: Move? = null

    val player = state.playerActive
    val possibleMoves = possibleMovesFromOrNull(state)
            ?: return BestMoveData(null, 0)

    if(randomize) possibleMoves.shuffle()
    possibleMoves.orderMoves(player)

    val hashMaker = ZobristHash(state.width, state.height)
    val tTable = TTable(hashMaker, 70000)

    var depthReached = 0

    for(i in 1..depth){
        val bestMove = pickBestMoveFullDepth(player, possibleMoves, state, i, deadline, tTable)
        if(isDeadlineCrossed(deadline)) break
        depthReached = i
        chosenMove = bestMove
    }
    return BestMoveData(chosenMove, depthReached)
}

private fun pickBestMoveFullDepth(player: Int, possibleMoves: List<Move>, state: GameState, depth: Int, deadline: Long? = null, tTable: TTable): Move? {
    var bestMove = possibleMoves[0]
    var bestScore = Int.MIN_VALUE + 1

    for (move in possibleMoves) {
        state.applyMove(move)
        val score = -negamax(state, depth - 1, -Int.MAX_VALUE, -bestScore, deadline, -player, tTable)
        state.undoMove(move)

        if (score > bestScore) {
            bestMove = move
            bestScore = score
        }
    }

    return bestMove
}