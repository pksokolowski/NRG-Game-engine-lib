package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.MAX_ENERGY
import com.github.pksokolowski.nrg.engine.utils.bound

fun MutableList<Move>.orderMoves(player: Int, bestMove: Move? = null): MutableList<Move> {
    intSort(player)

    bestMove?.let{
        if(remove(it)) add(0, it)
    }

    return this
}

private fun MutableList<Move>.intSort(player: Int): MutableList<Move> {
    val captures = Array<MutableList<Move>?>(MAX_ENERGY * 2 + 1) { null }
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