package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

@ExperimentalUnsignedTypes
class TTable(private val hashMaker: HashMaker, length: Int) {
    private val data = Array<TTableEntry?>(length) { null }
    private val length = length.toULong()

    operator fun get(gameState: GameState): TTableEntry {
        val hash = gameState.hash
        val index = (hash % length).toInt()

        fun matchOrNull() = data[index]?.let {
            if (it.hash == hash) it else null
        }

        fun create() = TTableEntry(hash).also {
            data[index] = it
        }

        return matchOrNull() ?: create()
    }
}