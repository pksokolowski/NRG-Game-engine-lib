package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.bound

fun MutableList<Move>.orderMoves(
    player: Int,
    bestMove: Move? = null,
    goodNonCaptures: List<Move>? = null
): MutableList<Move> {
    intSort(player)
    orderGoodNonCapturesAfterCaptures(goodNonCaptures)

    bestMove?.let {
        if (remove(it)) add(0, it)
    }

    return this
}

private fun MutableList<Move>.orderGoodNonCapturesAfterCaptures(
    goodNonCaptures: List<Move>?
) {
    if (!goodNonCaptures.isNullOrEmpty()) {
        val lastCaptureIndex = afterCapturesIndex(this)
        for (i in goodNonCaptures.indices) {
            val move = goodNonCaptures[i]
            val index = indexOf(move)
            if (index == -1) continue

            val destination = lastCaptureIndex + i
            if(destination > this.lastIndex) return

            this[destination] = move.also { this[index] = this[destination] }
        }
    }
}

private fun afterCapturesIndex(moves: List<Move>): Int {
    for (i in 0 until moves.size) {
        if (moves[i].capture == 0) return i
    }
    return 0
}

private fun MutableList<Move>.intSort(player: Int): MutableList<Move> {
    val captures = Array<MutableList<Move>?>(MAX_ENERGY + 1) { null }
    forEach {
        val priority = -player * it.capture
        val assignedIndex = priority.bound(0, MAX_ENERGY)

        if (captures[assignedIndex] == null) captures[assignedIndex] = mutableListOf()
        captures[assignedIndex]?.add(it)
    }

    clear()
    for (i in captures.lastIndex downTo 0) {
        val level = captures[i]
        if (level != null) {
            addAll(level)
        }
    }

    return this
}