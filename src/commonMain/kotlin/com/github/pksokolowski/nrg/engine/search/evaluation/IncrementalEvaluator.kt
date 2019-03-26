package com.github.pksokolowski.nrg.engine.search.evaluation

import com.github.pksokolowski.nrg.engine.Move

interface IncrementalEvaluator {
    infix fun apply(move: Move)
    infix fun undo(move: Move)
    fun evaluate(): Int
}