package com.github.pksokolowski.nrg.engine

data class Move(val movedPiece: Int,
                val x1: Int,
                val y1: Int,
                val x2: Int,
                val y2: Int,
                val capture: Int) {
    override fun toString() = "$movedPiece went from ($x1, $y1) to ($x2, $y2), capturing: $capture"
}