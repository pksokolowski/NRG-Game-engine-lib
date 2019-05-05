package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.Status
import com.github.pksokolowski.nrg.engine.Status.FINISHED
import com.github.pksokolowski.nrg.engine.Status.FINISHED.Player.*
import com.github.pksokolowski.nrg.engine.Status.ONGOING
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusSerializerTest {

    @Test
    fun `ONGOING status is serialized and deserialized back without data corruption`() =
        givenOngoingStatus()
            .serializerPreservesData()

    @Test
    fun `FINISHED status with human victory is serialized and deserialized back without data corruption`() =
        givenHumanVictory()
            .serializerPreservesData()

    @Test
    fun `FINISHED status with AI victory is serialized and deserialized back without data corruption`() =
        givenAiVictory()
            .serializerPreservesData()

    @Test
    fun `FINISHED status with a draw is serialized and deserialized back without data corruption`() =
        givenADraw()
            .serializerPreservesData()


    private fun givenHumanVictory() = FINISHED(HUMAN, 120)
    private fun givenAiVictory() = FINISHED(AI, -120)
    private fun givenADraw() = FINISHED(NOBODY, 0)
    private fun givenOngoingStatus() = ONGOING

    @UnstableDefault
    private fun Status.serializerPreservesData() {
        val serialized = Json.stringify(StatusSerializer, this)
        val deserialized = Json.parse(StatusSerializer, serialized)

        assertEquals(this, deserialized)
    }
}