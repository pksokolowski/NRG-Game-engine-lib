package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.transposition.NodeType.*

/**
 * A mutable transposition table entry.
 * Mutability is necessary to avoid recreation of the objects on index collisions.
 */
@ExperimentalUnsignedTypes
class TTableEntry(var hash: ULong) {
    var bestScore: Int = 0
        private set
    var bestMove: Move? = null
        private set
    var depth: Int = 0
        private set
    var type = EMPTY
        private set

    fun update(bestScore: Int, a: Int, b: Int, bestMove: Move?, depth: Int) {
        this.bestScore = bestScore
        // only store best moves for lower and exact nodes
        this.bestMove = if (bestScore > a) bestMove else null
        this.depth = depth

        type = when {
            bestScore <= a -> UPPER
            bestScore >= b -> LOWER
            else -> EXACT
        }
    }

}