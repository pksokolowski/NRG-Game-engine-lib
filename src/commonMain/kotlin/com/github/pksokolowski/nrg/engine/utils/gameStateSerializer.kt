package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.GameState
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializer(forClass = GameState::class)
object GameStateSerializer: KSerializer<GameState> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("GameState")

    override fun serialize(output: Encoder, obj: GameState) {
        val str = toString(obj)
        output.encodeString(str)
    }

    override fun deserialize(input: Decoder): GameState {
        val str = input.decodeString()
        return fromString(str)
    }

    private fun toString(gameState: GameState) = gameState.run {
        val builder = StringBuilder()

        builder.append("$height,$width,$movesCount|")

        forAllSquaresIndexed(width, height) iteration@{ x, y, _ ->
            builder.append(this[x, y])
            if (x == width - 1 && y == height - 1) return@iteration
            builder.append(",")
        }

        builder.toString()
    }

    private fun fromString(state: String): GameState {
        val parts = state.split("|")

        val variables = parts[0].split(",")
        val squares = parts[1].split(",")

        val height = variables[0].toInt()
        val width = variables[1].toInt()
        val movesCount = variables[2].toInt()

        val matrix = makeMatrix(width, height)
        forAllSquaresIndexed(width, height) { x, y, i ->
            matrix[x][y] = squares[i].toInt()
        }

        return GameState(matrix, movesCount)
    }

    private inline fun forAllSquaresIndexed(
        width: Int,
        height: Int,
        block: (x: Int, y: Int, i: Int) -> Unit
    ) {
        var i = 0
        for (x in 0 until width) for (y in 0 until height) {
            block(x, y, i++)
        }
    }
}


