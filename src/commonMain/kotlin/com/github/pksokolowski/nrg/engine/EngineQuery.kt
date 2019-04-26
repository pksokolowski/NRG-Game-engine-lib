package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.transposition.TTable

class EngineQuery(val state: GameState,
                  val timeAllowed: Long? = null,
                  val depthAllowed: Int = 30,
                  val randomize: Boolean = true,
                  val transpositionTTable: TTable? = null)