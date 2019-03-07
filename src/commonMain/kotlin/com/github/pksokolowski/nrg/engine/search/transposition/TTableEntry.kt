package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.Move

/**
 * A mutable transposition table entry.
 * Mutability is necessary to avoid recreation of the objects on index collisions.
 */
@ExperimentalUnsignedTypes
class TTableEntry(
    var hash: ULong,
    var bestScore: Int,
    var bestMove: Move?,
    var depth: Int,
    var type: NodeType
) {
    fun update(bestScore: Int, a: Int, b: Int, bestMove: Move, depth: Int) {
        this.bestScore = bestScore
        // only store best moves for lower and exact nodes
        this.bestMove = if (bestScore < b) bestMove else null
        this.depth = depth

        type = when {
            bestScore <= a -> {
                NodeType.LOWER
            }
            bestScore >= b -> {
                NodeType.UPPER
            }
            else -> {
                NodeType.EXACT
            }
        }
    }

    companion object {
        fun getEmpty(hash: ULong) = TTableEntry(hash, 0, null, 0, NodeType.EMPTY)
    }
}