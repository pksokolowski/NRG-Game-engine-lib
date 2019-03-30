package com.github.pksokolowski.nrg.engine.search.evaluation

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.search.evaluation.Evaluator.OPERATION.ADDITION
import com.github.pksokolowski.nrg.engine.search.evaluation.Evaluator.OPERATION.SUBTRACTION
import com.github.pksokolowski.nrg.engine.utils.MAX_SCORE
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE

class Evaluator(val state: GameState): IncrementalEvaluator{

    private var evalPos = evaluatePlayerMateriel(state, 1)
    private var evalNeg = evaluatePlayerMateriel(state, -1)

    override fun evaluate(): Int{
        if (evalPos == 0) return MIN_SCORE + state.movesCount
        if (evalNeg == 0) return MAX_SCORE - state.movesCount
        return evalPos + evalNeg
    }

    private fun dispatch(value: Int, operation: OPERATION){
        if(value > 0){
            evalPos += operation.sign * value
        }else if (value < 0) evalNeg += operation.sign * value
    }

    private inline fun add(value: Int) = dispatch(value, ADDITION)
    private inline fun subtract(value: Int) = dispatch(value, SUBTRACTION)

    override infix fun apply(move: Move) {
        subtract(move.movedPiece)
        subtract(move.capture)
        add(move.resolveDestinationValue())
    }

    override infix fun undo(move: Move) {
        subtract(move.resolveDestinationValue())
        add(move.movedPiece)
        add(move.capture)
    }

    private fun evaluatePlayerMateriel(state: GameState, player: Int): Int {
        var sum = 0
        state.forAllSquares iteration@{ x, y ->
            val square = state[x, y]
            if (square == 0) return@iteration
            if ((square > 0) xor (player > 0)) return@iteration

            sum += square
        }
        return sum
    }

    private enum class OPERATION(val sign: Int){
        ADDITION(1),
        SUBTRACTION(-1)
    }
}