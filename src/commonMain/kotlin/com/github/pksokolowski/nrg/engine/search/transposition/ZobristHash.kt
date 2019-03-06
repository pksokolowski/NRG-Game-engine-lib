package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState
import kotlin.random.Random
import kotlin.random.nextULong

@ExperimentalUnsignedTypes
class ZobristHash(width: Int, height: Int) {

    private val randoms = Array(width) { x ->
        ULongArray(height) { y ->
            Random.nextULong()
        }
    }
    private val playerOne = (10 + Random.nextInt()).toULong()

    fun hashOf(gameState: GameState): ULong {
        var hash = 0UL
        for (x in 0 until gameState.width)
            for (y in 0 until gameState.height) {
                val square = gameState[x,y]
                if(square == 0) continue
                hash += randoms[x][y] + gameState[x, y].toULong()
            }
        if(gameState.playerActive == 1) hash += playerOne
        return hash
    }
}