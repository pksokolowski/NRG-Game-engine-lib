package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

@ExperimentalUnsignedTypes
class TTable(private val hashMaker: HashMaker, length: Int) {
    private val data = Array<TTableEntry?>(length) { null }
    private val length = length.toULong()

    operator fun get(gameState: GameState): TTableEntry {
        val hash = hashMaker.hashOf(gameState)
        val index = indexOf(hash)

        return matchOrNull(index, hash)
            ?: create(index, hash)
    }

    private fun matchOrNull(index: Int, hash: ULong) = data[index]?.let {
        if (it.hash == hash) it else null
    }

    private fun create(index: Int, hash: ULong) = TTableEntry.getEmpty(hash).also {
        data[index] = it
    }

    private fun indexOf(hash: ULong) = (hash % length).toInt()
}