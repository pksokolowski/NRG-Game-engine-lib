package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.evaluation.IncrementalEvaluatorFactory
import com.github.pksokolowski.nrg.engine.search.transposition.IncrementalHasherFactory

class GameState(
    private val board: Array<IntArray>,
    movesCount: Int = 0,
    evaluatorFactory: IncrementalEvaluatorFactory = IncrementalEvaluatorFactory(),
    hasherFactory: IncrementalHasherFactory = IncrementalHasherFactory()
) {
    val width = board.size
    val height = board.getOrNull(0)?.size ?: 0
    var movesCount: Int = movesCount; private set
    val playerActive: Int get() = if (movesCount % 2 == 0) -1 else 1

    private val evaluator = evaluatorFactory.getEvaluator(this)

    fun evaluate() = evaluator.evaluate()
    fun evaluateForActivePlayer() = evaluator.evaluate() * playerActive

    private val hashMaker = hasherFactory.getIncrementalHasher(this)
    val hash: ULong
        get(){
//            val hashFull = hashMaker.hashOf(this)
//            val hashIncc = hashMaker.getHash()
//            val areTheSame = hashFull == hashIncc
//            if(!areTheSame)
//                throw RuntimeException("incremental hashing came up with a different hash than the old solution.")
            return hashMaker.getHash()
        }

    private operator fun set(x: Int, y: Int, value: Int) {
        board[x][y] = value
    }

    operator fun get(x: Int, y: Int) = board[x][y]

    internal fun applyMove(move: Move) {
        require(this[move.x1, move.y1] != 0) { "Attempted to move a nonexistent piece." }

        this[move.x1, move.y1] = 0
        this[move.x2, move.y2] = move.resolveDestinationValue()
        movesCount++

        evaluator apply move
        hashMaker apply move
    }

    internal fun undoMove(move: Move) {
        require(this[move.x2, move.y2] != 0) { "Attempted to undo a move of a nonexistent piece." }

        this[move.x1, move.y1] = move.movedPiece
        this[move.x2, move.y2] = move.capture
        movesCount--

        evaluator undo move
        hashMaker undo move
    }

    internal fun applyNullMove(){
        movesCount++
        hashMaker apply null
    }

    internal fun undoNullMove(){
        movesCount--
        hashMaker undo null
    }

    fun getBoard() = Array(board.size) { board[it].copyOf() }

    fun copy() = GameState(getBoard(), movesCount)

    fun withMove(move: Move) = copy().apply { applyMove(move) }

    inline fun forAllSquares(block: (x: Int, y: Int) -> Unit) {
        for (x in 0 until width) for (y in 0 until height) {
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