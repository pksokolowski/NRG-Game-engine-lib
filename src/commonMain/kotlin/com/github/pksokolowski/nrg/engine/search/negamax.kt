package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.NodeType
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed
import kotlin.math.max
import kotlin.math.min

fun negamax(state: GameState, depthLeft: Int, absoluteDepth: Int, alpha: Int, beta: Int, deadline: Long?, player: Int, tTable: TTable, killers: KillerHeuristic): Int {
    if (isDeadlineCrossed(deadline)) return MIN_SCORE

    if (depthLeft == 0) return state.evaluateForActivePlayer()
    var newA = alpha
    var newB = beta

    var bestScore = Int.MIN_VALUE + 1
    var nullableBestMove: Move? = null
    fun Move?.givesCutOff(): Boolean{
        if(this == null) return false
        if(!(this legalIn state)) return false
        state.applyMove(this)
        val score = -negamax(state, depthLeft - 1, absoluteDepth + 1, -newB, -newA, deadline, -player, tTable, killers)
        state.undoMove(this)
        if (score > bestScore) {
            bestScore = score
            nullableBestMove = this
        }
        newA = max(score, newA)
        if(newA >= newB) return true
        return false
    }

    val ttEntry = tTable[state]
    if (ttEntry.depth >= depthLeft) {
        when (ttEntry.type) {
            NodeType.EXACT -> return ttEntry.bestScore
            NodeType.LOWER -> newA = max(alpha, ttEntry.bestScore)
            NodeType.UPPER -> newB = min(beta, ttEntry.bestScore)
        }
        if(newA >= newB) return ttEntry.bestScore
    }

    if(ttEntry.bestMove.givesCutOff()){
        return newB
    }

    val killerMoves = killers recallAt absoluteDepth

    if(killerMoves.getOrNull(0).givesCutOff()){
        return newB
    }
    if(killerMoves.getOrNull(1).givesCutOff()){
        return newB
    }

    val moves = possibleMovesFromOrNull(state)?.orderMoves(player)
        ?: return state.evaluateForActivePlayer()

    moves.remove(ttEntry.bestMove)

    var bestMove = nullableBestMove ?: moves[0]
    for (it in moves) {
        state.applyMove(it)
        val score = -negamax(state, depthLeft - 1, absoluteDepth + 1, -newB, -newA, deadline, -player, tTable, killers)
        if (score > bestScore) {
            bestScore = score
            bestMove = it
        }
        state.undoMove(it)
        newA = max(bestScore, newA)
        if (newA >= newB) {
            killers.remember(it, absoluteDepth)
            break
        }
    }

    ttEntry.update(bestScore, alpha, beta, bestMove, depthLeft)

    return bestScore
}