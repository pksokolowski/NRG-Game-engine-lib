package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.EngineResponse
import com.github.pksokolowski.nrg.engine.startGame
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class EngineResponseSerializationTest {

    @Test
    fun `kotlin serializer works correctly for EngineResponse`() {
        val response = startGame()
        val serialized = Json.stringify(EngineResponse.serializer(), response)
        val deserialized = Json.parse(EngineResponse.serializer(), serialized)

        assertEquals(response, deserialized)
    }

    @Test
    fun `serialize() and companion's deserialize() work as expected`() {
        val response = startGame()
        val serialized = response.serialize()
        val deserialized = EngineResponse.deserialize(serialized)

        assertEquals(response, deserialized)
    }
}