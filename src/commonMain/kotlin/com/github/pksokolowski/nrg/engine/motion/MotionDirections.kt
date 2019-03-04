package com.github.pksokolowski.nrg.engine.motion

enum class MotionDirections(val x: Int, val y: Int) {
    FORWARD(0, 1),
    FORWARD_LEFT(-1, 1),
    FORWARD_RIGHT(1, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    BACKWARDS(0, -1),
    BACKWARDS_LEFT(-1, -1),
    BACKWARDS_RIGHT(1, -1);

    operator fun component1() = x
    operator fun component2() = y
}