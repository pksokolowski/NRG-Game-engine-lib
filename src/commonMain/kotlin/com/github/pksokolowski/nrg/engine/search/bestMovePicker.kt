package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.search.transposition.ZobristHash
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.getDeadline
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed

class BestMoveData(val move: Move?, val depthReached: Int, val transpositionTable: TTable?)

fun pickBestMoveFrom(
    state: GameState,
    depth: Int,
    timeLimit: Long? = null,
    randomize: Boolean = false,
    transpositionTTable: TTable? = null
): BestMoveData {
    // work on a copy, so the library user can still enjoy a practically immutable object
    val stateCopy = state.copy()

    val possibleMoves = getPossibleMovesAtRoot(stateCopy, randomize)
        ?: return BestMoveData(null, 0, null)

    var depthReached = 0
    var chosenMove: Move? = null
    val deadline = getDeadline(timeLimit)
    val tTable = transpositionTTable ?: getTranspositionTable(stateCopy)
    val killers = KillerHeuristic(depth)

    for (i in 1..depth) {
        val bestMove = pickBestMove(possibleMoves, stateCopy, i, deadline, tTable, killers)
        if (isDeadlineCrossed(deadline)) break
        depthReached = i
        chosenMove = bestMove
    }
    return BestMoveData(chosenMove, depthReached, tTable)
}

private fun pickBestMove(
    possibleMoves: List<Move>,
    state: GameState,
    depth: Int,
    deadline: Long? = null,
    tTable: TTable,
    killers: KillerHeuristic
): Move? {
    val player = state.playerActive
    var bestMove = possibleMoves[0]
    var bestScore = MIN_SCORE

    for (move in possibleMoves) {
        state.applyMove(move)
        val score = -negamax(state, depth - 1, 1, -MAX_SCORE, -bestScore, deadline, -player, tTable, killers)
        state.undoMove(move)

        if (score > bestScore) {
            bestMove = move
            bestScore = score
        }
    }

    return bestMove
}

private fun getTranspositionTable(state: GameState): TTable {
    val hashMaker = ZobristHash(state.width, state.height)
    return TTable(hashMaker, 7000000)
}

private fun getPossibleMovesAtRoot(state: GameState, randomize: Boolean): List<Move>? {
    val player = state.playerActive
    val possibleMoves = possibleMovesFromOrNull(state)
        ?: return null

    if (randomize) possibleMoves.shuffle()
    possibleMoves.orderMoves(player)
    return possibleMoves
}