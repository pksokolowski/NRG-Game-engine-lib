package com.github.pksokolowski.nrg.engine.search.transposition

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.getInitialGameState
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextULong

@ExperimentalUnsignedTypes
class ZobristHash(width: Int, height: Int, val state: GameState = getInitialGameState()) : HashMaker {
//todo remove the default param for state above ^^

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

    private var hash = hashOf(state)
    override fun getHash() = hash

    override infix fun apply(move: Move?) {
        applyPlayerHash()
        move?.run {
            hash -= pieceValue(x1, y1, movedPiece)
            hash -= pieceValue(x2, y2, capture)
            hash += pieceValue(x2, y2, resolveDestinationValue())
        }
    }

    override infix fun undo(move: Move?) {
        applyPlayerHash()
        move?.run {
            hash -= pieceValue(x2, y2, resolveDestinationValue())
            hash += pieceValue(x1, y1, movedPiece)
            hash += pieceValue(x2, y2, capture)
        }
    }

    private fun pieceValue(x: Int, y: Int, energy: Int): ULong{
        if(energy == 0) return 0UL
        return randoms[x][y] * (energy.toULong() + pieces[abs(energy)])
    }

    private fun applyPlayerHash() {
        if (state.playerActive == 1) hash += playerOne
        else hash -= playerOne
    }
}