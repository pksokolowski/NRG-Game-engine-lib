package com.github.pksokolowski.nrg.engine.search.evaluation

import com.github.pksokolowski.nrg.engine.GameState

class IncrementalEvaluatorFactory {
    fun getEvaluator(state: GameState): IncrementalEvaluator {
        return Evaluator(state)
    }
}