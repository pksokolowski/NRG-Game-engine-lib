package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.NodeType
import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.isDeadlineCrossed
import kotlin.math.max
import kotlin.math.min

fun negamax(state: GameState, depthLeft: Int, absoluteDepth: Int, alpha: Int, beta: Int, deadline: Long?, player: Int, tTable: TTable, killers: KillerHeuristic, allowNullMuve: Boolean = true): Int {
    if (depthLeft == 0) return state.evaluateForActivePlayer()
    if (isDeadlineCrossed(deadline)) return MIN_SCORE

    var newA = alpha
    var newB = beta

    var bestScore = Int.MIN_VALUE + 1
    var bestMove: Move? = null

    val ttEntry = tTable[state]

    if (ttEntry.depth >= depthLeft) {
        when (ttEntry.type) {
            NodeType.EXACT -> return ttEntry.bestScore
            NodeType.LOWER -> newA = max(alpha, ttEntry.bestScore)
            NodeType.UPPER -> newB = min(beta, ttEntry.bestScore)
        }
        if(newA >= newB) return ttEntry.bestScore
    }

    fun recursiveCall(remainingDepth: Int, shouldAllowNullMove: Boolean = allowNullMuve) =
        - negamax(state, remainingDepth, absoluteDepth + 1, -newB, -newA, deadline, -player, tTable, killers, shouldAllowNullMove)

    if (allowNullMuve && depthLeft - 3 > 0) {
        state.applyNullMove()
        val score = recursiveCall(depthLeft - 3, false)
        state.undoNullMove()
        if (score >= newB) return score
    }

    fun scoreFor(move: Move, reduceLateMoves: Boolean = false, moveNum: Int = -1) {
        state.applyMove(move)
        val reduction = if(moveNum > 6) 3 else 2
        val depthLeftToUse = if (
            reduceLateMoves &&
            ttEntry.type != NodeType.EXACT &&
            absoluteDepth >= 3 &&
            move.capture == 0
        ) {
            max(1, depthLeft - reduction)
        } else depthLeft

        var score = recursiveCall(depthLeftToUse - 1)
        if (depthLeft != depthLeftToUse && score > newA)
            score = recursiveCall(depthLeftToUse - 1)

        state.undoMove(move)
        if (score > bestScore) {
            bestScore = score
            bestMove = move
        }
        newA = max(score, newA)
    }

    fun Move?.givesCutoff(): Boolean {
        if (this == null || this notLegalIn state) return false
        scoreFor(this)
        return newA >= newB
    }

    fun List<Move>.giveCutoff() = any { it.givesCutoff() }

    if (ttEntry.bestMove.givesCutoff() ||
        (killers at absoluteDepth).giveCutoff()
    ) return newB

    val moves = possibleMovesFromOrNull(state)?.orderMoves(player, ttEntry.bestMove)
        ?: return state.evaluateForActivePlayer()

    for ((i, it) in moves.withIndex()) {
        scoreFor(it, i > 3, i)
        if (newA >= newB) {
            killers.remember(it, absoluteDepth)
            break
        }
    }

    ttEntry.update(bestScore, alpha, beta, bestMove, depthLeft)

    return bestScore
}