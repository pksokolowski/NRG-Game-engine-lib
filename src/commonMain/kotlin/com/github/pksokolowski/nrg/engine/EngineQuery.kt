package com.github.pksokolowski.nrg.engine

class EngineQuery(val state: GameState,
                  val timeAllowed: Long? = null,
                  val depthAllowed: Int = 30,
                  val randomize: Boolean = true)