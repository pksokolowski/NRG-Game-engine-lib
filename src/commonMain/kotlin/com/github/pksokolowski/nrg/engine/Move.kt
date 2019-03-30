package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.bound
import kotlin.math.abs

data class Move(
    val movedPiece: Int,
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
    val capture: Int
) {
    /**
     * Computes the value of the square (x2, y2) after the application
     * of the move. Takes into account the maximum possible energy level
     * or a square.
     */
    fun resolveDestinationValue(): Int {
        val player = if (movedPiece > 0) 1 else -1
        return (movedPiece + abs(capture) * player).bound(-MAX_ENERGY, MAX_ENERGY)
    }

    infix fun notLegalIn(state: GameState) = state[x1, y1] != movedPiece || state[x2, y2] != capture

    override fun toString() = "$movedPiece went from ($x1, $y1) to ($x2, $y2), capturing: $capture"
}