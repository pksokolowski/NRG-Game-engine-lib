package com.github.pksokolowski.nrg.engine

class EngineResponse(val state: GameState,
                     val possibleMoves: List<Move>,
                     val depthReached: Int)