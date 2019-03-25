package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.utils.deserialize
import com.github.pksokolowski.nrg.engine.utils.serialize
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals

class GameStateSerializerTest{
    @Test
    fun `deserializes to the same object`(){
        val state = """
            00 +1 +2 -3 00
            00 00 00 00 -1
            00 00 00 +4 -4
            -3 -2 -1 00 00
        """.toGameState(4)

        val serialized = serialize(state)
        val deserialized = deserialize(serialized)

        assertEquals(state, deserialized)
    }
}