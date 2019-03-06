package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

@ExperimentalUnsignedTypes
class TTable(gameState: GameState, val length: Int) {
    private val data = Array<TTableEntry?>(length) { null }
    private val hashMaker = ZobristHash(gameState.width, gameState.height)

    operator fun get(gameState: GameState): TTableEntry? {
        return data[indexOf(gameState)]
    }

    operator fun set(gameState: GameState, entry: TTableEntry) {
        data[indexOf(gameState)] = entry
    }

    private fun indexOf(gameState: GameState): Int {
        val hash = hashMaker.hashOf(gameState)
        val index = hash % length.toULong()
        return index.toInt()
    }
}