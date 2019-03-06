package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.Move

/**
 * A mutable transposition table entry.
 * Mutability is necessary to avoid recreation of the objects on index collisions.
 */
@ExperimentalUnsignedTypes
class TTableEntry(
    var hash: ULong,
    var bestMove: Move?,
    var depth: Int,
    var type: NodeType
)