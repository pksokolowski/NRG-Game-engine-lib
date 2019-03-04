package com.github.pksokolowski.nrg.engine

class EngineQuery(val state: GameState,
                  val chosenMove: Move,
                  val timeAllowed: Long? = null,
                  val depthAllowed: Int = Int.MAX_VALUE,
                  val randomize: Boolean = true)