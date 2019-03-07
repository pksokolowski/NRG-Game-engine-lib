package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState

@ExperimentalUnsignedTypes
class TTable(gameState: GameState, val length: Int) {
    private val data = Array<TTableEntry?>(length) { null }
    private val hashMaker = ZobristHash(gameState.width, gameState.height)

    operator fun get(gameState: GameState): TTableEntry {
        val hash = hashMaker.hashOf(gameState)
        val index = indexOf(hash)

        return matchOrNull(index, hash)
            ?: create(index, hash)
    }

    fun clear(){
        for(i in data.indices) data[i] = null
    }

//    fun calcLoad(): String{
//        var found = 0
//        for(i in data.indices) if(data[i] != null) found++
//        val perc =(found / data.size.toDouble()) * 100
//        return "TTable used in: ${perc.toInt()}%"
//    }

    private fun matchOrNull(index: Int, hash: ULong): TTableEntry? {
        val entry = data[index] ?: return null
        return if (entry.hash == hash) entry else null
    }

    private fun create(index: Int, hash: ULong) =
        TTableEntry.getEmpty(hash).also {
            data[index] = it
            return it
        }

    private fun indexOf(hash: ULong): Int {
        val index = hash % length.toULong()
        return index.toInt()
    }
}