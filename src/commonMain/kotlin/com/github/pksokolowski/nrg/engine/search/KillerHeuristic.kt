package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move

class KillerHeuristic(maxDepth: Int) {
    private val moves = Array(maxDepth) { Array<Move?>(2) { null } }
    private val turns = IntArray(maxDepth)

    fun remember(move: Move, depth: Int) {
        if (move.capture != 0) return
        if (moves[depth].contains(move)) return

        // add move in the slot whose turn it is now
        moves[depth][turns[depth]] = move

        // increment turn counter for that depth
        turns[depth] = (turns[depth] + 1) % moves[depth].size
    }

    infix fun recallAt(depth: Int): List<Move> = mutableListOf<Move>().apply {
        moves[depth].forEach { if(it != null) this.add(it) }
    }
}