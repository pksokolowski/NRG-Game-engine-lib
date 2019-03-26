package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.evaluatePlayerMateriel
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import com.github.pksokolowski.nrg.engine.utils.bound
import kotlin.math.abs

class GameState(private val board: Array<IntArray>, movesCount: Int = 0) {
    val width = board.size
    val height = board.getOrNull(0)?.size ?: 0
    var movesCount: Int = movesCount; private set
    val playerActive: Int get() = if (movesCount % 2 == 0) -1 else 1

    private operator fun set(x: Int, y: Int, value: Int) {
        board[x][y] = value
    }

    operator fun get(x: Int, y: Int) =  board[x][y]

    private var evalPos = evaluatePlayerMateriel(this, 1)
    private var evalNeg = evaluatePlayerMateriel(this, -1)

    fun getEvaluation(): Int{
        if (evalPos == 0) return MIN_SCORE + this.movesCount
        if (evalNeg == 0) return MAX_SCORE - this.movesCount
        return evalPos + evalNeg
    }
    private fun dispatchAddition(value: Int){
        if(value > 0){
            evalPos += value
        }else if (value < 0) evalNeg += value
    }
    private fun dispatchSubtraction(value: Int){
        if(value > 0){
            evalPos -= value
        }else if (value < 0) evalNeg -= value
    }

    internal fun applyMove(move: Move) {
        require(this[move.x1, move.y1] != 0) { "Attempted to move a nonexistent piece." }

        val player = if (move.movedPiece > 0) 1 else -1
        this[move.x1, move.y1] = 0
        this[move.x2, move.y2] = (move.movedPiece + abs(move.capture) * player).bound(-MAX_ENERGY, MAX_ENERGY)
        movesCount++

        dispatchSubtraction(move.movedPiece)
        dispatchSubtraction(move.capture)
        dispatchAddition(this[move.x2, move.y2])
    }

    internal fun undoMove(move: Move) {
        require(this[move.x2, move.y2] != 0) { "Attempted to undo a move of a nonexistent piece." }

        dispatchSubtraction(this[move.x2, move.y2])
        dispatchAddition(move.movedPiece)
        dispatchAddition(move.capture)

        this[move.x1, move.y1] = move.movedPiece
        this[move.x2, move.y2] = move.capture
        movesCount--
    }

    fun getBoard() = Array(board.size) { board[it].copyOf() }

    fun copy() = GameState(getBoard(), movesCount)

    fun withMove(move: Move) = copy().apply { applyMove(move) }

    inline fun forAllSquares(block: (x: Int, y: Int) -> Unit) {
        for(x in 0 until width) for(y in 0 until height){
            block(x, y)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameState) return false

        if (!board.contentDeepEquals(other.board)) return false
        if (movesCount != other.movesCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + movesCount
        return result
    }
}