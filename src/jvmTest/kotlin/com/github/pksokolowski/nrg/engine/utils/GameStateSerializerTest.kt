package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.GameState
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class GameStateSerializerTest {

    @Test
    fun `kotlin serializer works correctly for GameState`() =
        givenSimpleGameState()
            .serializationCycleIntroducesNoDataCorruption()


    private fun givenSimpleGameState() = """
            00 +1 +2 -3 00
            00 00 00 00 -1
            00 00 00 +4 -4
            -3 -2 -1 00 00
        """.toGameState(4)

    @UnstableDefault
    private fun GameState.serializationCycleIntroducesNoDataCorruption() {
        val serialized = Json.stringify(GameStateSerializer, this)
        val deserialized = Json.parse(GameStateSerializer, serialized)

        assertEquals(this, deserialized)
    }
}