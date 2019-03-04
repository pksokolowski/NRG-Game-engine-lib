package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.bound
import kotlin.math.abs

class GameState(private val board: Array<IntArray>, movesCount: Int = 0) {

    fun copy() = GameState(getBoard(), movesCount)

    fun getBoard() = Array(board.size) { board[it].copyOf() }

    var movesCount: Int = movesCount
        private set
    val playerActive: Int
        get() {
            return if (movesCount % 2 == 0) -1 else 1
        }
    val width = board.size
    val height = board.getOrNull(0)?.size ?: 0

    private operator fun set(x: Int, y: Int, value: Int) {
        board[x][y] = value
    }

    operator fun get(x: Int, y: Int): Int {
        return board[x][y]
    }

    internal fun applyMove(move: Move) {
        require(this[move.x1, move.y1] != 0) { "Attempted to move a nonexistent piece." }

        val player = if (move.movedPiece > 0) 1 else -1
        this[move.x1, move.y1] = 0
        this[move.x2, move.y2] = (move.movedPiece + abs(move.capture) * player).bound(-MAX_ENERGY, MAX_ENERGY)
        movesCount++
    }

    internal fun undoMove(move: Move) {
        require(this[move.x2, move.y2] != 0) { "Attempted to undo a move of a nonexistent piece." }

        this[move.x1, move.y1] = move.movedPiece
        this[move.x2, move.y2] = move.capture
        movesCount--
    }

    fun withMove(move: Move) = copy().apply { applyMove(move) }
}