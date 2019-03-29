package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move

class KillerHeuristic(maxDepth: Int) {
    private val moves = Array(maxDepth) { Array<Move?>(2) { null } }
    private val turns = IntArray(maxDepth)

    fun remember(move: Move, depth: Int) {
        if (move.capture != 0) return
        if (moves[depth].contains(move)) return

        // add move in the slot whose turn it is now
        moves[depth][turns[depth]] = move

        // increment turn counter for that depth
        turns[depth] = (turns[depth] + 1) % moves[depth].size
    }

    infix fun recallAt(depth: Int): List<Move> = mutableListOf<Move>().apply {
        moves[depth].forEach { if(it != null) this.add(it) }
    }

    fun orderMoves(depth: Int, startIndex: Int, movesToOrder: MutableList<Move>) {
        moves[depth][0]?.let let@{
            val index = movesToOrder.indexOf(it)
            if(index == -1) return@let
            movesToOrder[startIndex] = it.also { movesToOrder[index] = movesToOrder[startIndex] }
        }
        moves[depth][1]?.let let@{
            val index = movesToOrder.indexOf(it)
            if(index == -1) return@let
            if(movesToOrder.lastIndex < startIndex+1) return@let
            movesToOrder[startIndex+1] = it.also { movesToOrder[index] = movesToOrder[startIndex+1] }
        }

//        val killers = this recallAt depth
//        if (!killers.isNullOrEmpty()) {
//            val lastCaptureIndex = startIndex
//            for (i in killers.indices) {
//                val move = killers[i]
//                val index = movesToOrder.indexOf(move)
//                if (index == -1) continue
//
//                val destination = lastCaptureIndex + i
//                if(destination > movesToOrder.lastIndex) return
//
//                movesToOrder[destination] = move.also { movesToOrder[index] = movesToOrder[destination] }
//            }
//        }
    }
}