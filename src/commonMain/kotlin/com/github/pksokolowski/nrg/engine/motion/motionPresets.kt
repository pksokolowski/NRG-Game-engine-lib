package com.github.pksokolowski.nrg.engine.motion

import com.github.pksokolowski.nrg.engine.motion.MotionDirections.*
import com.github.pksokolowski.nrg.engine.motion.MoveTypes.*

fun presetCapturesForward(add: (direction: MotionDirections, type: MoveTypes, range: IntRange) -> Unit) {
    val range = 1..1
    add(FORWARD_LEFT, CAPTURE_ONLY, range)
    add(FORWARD_RIGHT, CAPTURE_ONLY, range)
}

fun presetCapturesForwardAndBackwards(add: (direction: MotionDirections, type: MoveTypes, range: IntRange) -> Unit) {
    val range = 1..1
    presetCapturesForward(add)
    add(BACKWARDS_LEFT, CAPTURE_ONLY, range)
    add(BACKWARDS_RIGHT, CAPTURE_ONLY, range)
}

fun presetVerticalAndHorizontal(add: (direction: MotionDirections, type: MoveTypes, range: IntRange) -> Unit,
                                range: IntRange) {
    add(FORWARD, ANY, range)
    add(BACKWARDS, ANY, range)
    add(LEFT, ANY, range)
    add(RIGHT, ANY, range)
}

fun presetDiagonal(add: (direction: MotionDirections, type: MoveTypes, range: IntRange) -> Unit,
                   range: IntRange) {
    add(FORWARD_LEFT, ANY, range)
    add(FORWARD_RIGHT, ANY, range)
    add(BACKWARDS_LEFT, ANY, range)
    add(BACKWARDS_RIGHT, ANY, range)
}