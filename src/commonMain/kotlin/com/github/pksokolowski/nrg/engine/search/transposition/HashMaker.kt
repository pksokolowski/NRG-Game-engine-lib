package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

interface HashMaker{
    fun hashOf(gameState: GameState): ULong
}