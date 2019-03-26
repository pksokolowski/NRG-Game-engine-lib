package com.github.pksokolowski.nrg.engine.search.evaluation

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
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

    override infix fun apply(move: Move) {
        dispatchSubtraction(move.movedPiece)
        dispatchSubtraction(move.capture)
        dispatchAddition(move.resolveDestinationValue())
    }

    override infix fun undo(move: Move) {
        dispatchSubtraction(move.resolveDestinationValue())
        dispatchAddition(move.movedPiece)
        dispatchAddition(move.capture)
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
}