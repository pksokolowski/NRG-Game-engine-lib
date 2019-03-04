package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.GameState

/**
 * parses a multi-line string into a board of a GameState.
 *
 * takes input like:
 *
 * |00 +1 00
 * |00 00 -2
 * |00 -1 00
 *
 * where | (pipe) is new line startGame.
 * Indexes for x and y rise left-right and bottom-top respectively
 */
fun String.toGameState(movesCount: Int = 0): GameState {
    val trimmedInput = this.trimIndent()
    return generateState(trimmedInput, movesCount)
}

private fun generateState(board: String, movesCount: Int = 0): GameState {
    val lines = board.split("\n")
    val sampleColumn = lines[0].split(" ")

    val output = Array(sampleColumn.size) { IntArray(lines.size) }

    for (i in lines.indices) {
        val columns = lines[i].split(" ")
        for (j in 0 until columns.size) {
            output[j][i] = columns[j].toInt()
        }
    }
    return GameState(output, movesCount)
}