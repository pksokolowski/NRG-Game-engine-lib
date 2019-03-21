package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextULong

@ExperimentalUnsignedTypes
class ZobristHash(width: Int, height: Int) : HashMaker {

    private val randoms = Array(width) { x ->
        ULongArray(height) { y ->
            Random.nextULong()
        }
    }
    private val playerOne = (10 + Random.nextInt()).toULong()
    private val pieces = ULongArray(MAX_ENERGY + 2) { Random.nextULong() }

    override fun hashOf(gameState: GameState): ULong {
        var hash = 0UL
        gameState.forAllSquares iteration@{ x, y ->
            val square = gameState[x, y]
            if (square == 0) return@iteration
            hash += randoms[x][y] * (square.toULong() + pieces[abs(square)])
        }
        if (gameState.playerActive == 1) hash += playerOne
        return hash
    }
}