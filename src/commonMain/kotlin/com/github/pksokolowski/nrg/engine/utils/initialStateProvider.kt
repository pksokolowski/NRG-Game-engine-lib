package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.GameState

fun getInitialGameState(): GameState {
    return """
        +1 +1 +1 +1 +1 +1 +1 +1
        +1 +1 +1 +1 +1 +1 +1 +1
        00 00 00 00 00 00 00 00
        00 00 00 00 00 00 00 00
        00 00 00 00 00 00 00 00
        00 00 00 00 00 00 00 00
        -1 -1 -1 -1 -1 -1 -1 -1
        -1 -1 -1 -1 -1 -1 -1 -1
    """.toGameState()
}