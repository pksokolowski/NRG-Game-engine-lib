package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.GameState
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.motion.*
import com.github.pksokolowski.nrg.engine.motion.MotionDirections.*
import com.github.pksokolowski.nrg.engine.motion.MoveTypes.*
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import kotlin.math.abs

fun possibleMovesFrom(state: GameState): MutableList<Move> {
    val moves = mutableListOf<Move>()
    val player = state.playerActive

    for (x in 0 until state.width) for (y in 0 until state.height) {
        // skip empty squares
        val value = state[x, y]
        if (value == 0) continue

        // skip pieces of the other player
        val owner = if (value > 0) 1 else -1
        if (owner != player) continue

        fun add(direction: MotionDirections, type: MoveTypes, stepsRange: IntRange = 1..1) =
            addToList(state, player, moves, x, y, direction, type, stepsRange)

        val energy = abs(value)

        when (energy) {
            1 -> {
                add(FORWARD, NON_CAPTURE)
                presetCapturesForward(::add)
            }
            2 -> {
                add(FORWARD, NON_CAPTURE)
                add(BACKWARDS, NON_CAPTURE)
                presetCapturesForwardAndBackwards(::add)
            }
            3 -> {
                presetCapturesForwardAndBackwards(::add)
                presetVerticalAndHorizontal(::add, 1..2)
            }
            MAX_ENERGY -> {
                presetVerticalAndHorizontal(::add, 1..2)
                presetDiagonal(::add, 1..4)
            }
            else -> {
            }
        }
    }

    return moves
}

fun possibleMovesFromOrNull(state: GameState): MutableList<Move>? {
    val moves = possibleMovesFrom(state)
    return if (moves.isEmpty()) null else moves
}

private fun addToList(
    state: GameState,
    player: Int,
    list: MutableList<Move>,
    x: Int,
    y: Int,
    direction: MotionDirections,
    type: MoveTypes,
    stepsRange: IntRange
) {
    val (dx, dy) = direction

    for (s in stepsRange) {
        val destinationX = x + (s * dx * player)
        val destinationY = y + (s * dy * player)
        if (destinationX !in 0 until state.width || destinationY !in 0 until state.height) return
        val initialValue = state[x, y]
        val destinationValue = state[destinationX, destinationY]

        if (type == CAPTURE_ONLY && destinationValue == 0) continue
        if (type == NON_CAPTURE && destinationValue != 0) break

        val move = Move(initialValue, x, y, destinationX, destinationY, destinationValue)
        list.add(move)

        // only the closest piece in a straight line can be captured. You can't choose to jump over it
        // and capture what's behind instead.
        if (move.capture != 0) return
    }
}