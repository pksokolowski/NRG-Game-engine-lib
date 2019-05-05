package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.search.transposition.TTable
import com.github.pksokolowski.nrg.engine.utils.GameStateSerializer
import com.github.pksokolowski.nrg.engine.utils.StatusSerializer
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

/**
 * Contains response of the engine, either resulting from startGame() or play() calls.
 * The response contains state of the game and possible moves human user can make, as well
 * as the status of the game, ONGOING or FINISHED. Materiel evaluation can be obtained from
 * the GameState instance.
 *
 * In order to serialize/deserialze instances of this class, use serialize method on them or
 * deserialize method in the companion object.
 *
 * @param state game state after the engine performed a move or, in case of game beginning, the initial game state
 * @param possibleMoves a list of moves that are legal for the human player in the current state of the game
 * @param depthReached information about how deeply the engine managed to search.
 * @param status status of the game at the moment
 * @param transpositionTTable a transposition table used in the generation of this response. Can be reused for
 * future queries for performance benefits.
 */
@Serializable
data class EngineResponse(
    @Serializable(with = GameStateSerializer::class)
    val state: GameState,
    val possibleMoves: List<Move>,
    val depthReached: Int,
    @Serializable(with = StatusSerializer::class)
    val status: Status,
    @Transient
    val transpositionTTable: TTable? = null
) {
    @UnstableDefault
    fun serialize() = Json.stringify(serializer(), this)

    companion object {
        @UnstableDefault
        fun deserialize(serializedResponse: String) = Json.parse(serializer(), serializedResponse)
    }
}