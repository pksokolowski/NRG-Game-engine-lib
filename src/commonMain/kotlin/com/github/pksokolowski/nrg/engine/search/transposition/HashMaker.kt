package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move

interface HashMaker{
    fun hashOf(gameState: GameState): ULong
    fun getHash(): ULong
    infix fun apply(move: Move?)
    infix fun undo(move: Move?)
}