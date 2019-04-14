package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

class IncrementalHasherFactory{
    fun getIncrementalHasher(state: GameState): HashMaker{
        return ZobristHash(state.width, state.height, state)
    }
}