package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.transposition.TTable

/**
 * @param state game state after the engine performed a move or, in case of game beginning, the initial game state
 * @param possibleMoves a list of moves that are legal for the human player in the current state of the game
 * @param depthReached information about how deeply the engine managed to search.
 * @param status status of the game at the moment
 * @param transpositionTTable a transposition table used in the generation of this response. Can be reused for
 * future queries for performance benefits.
 */
class EngineResponse(val state: GameState,
                     val possibleMoves: List<Move>,
                     val depthReached: Int,
                     val status: Status,
                     val transpositionTTable: TTable? = null)