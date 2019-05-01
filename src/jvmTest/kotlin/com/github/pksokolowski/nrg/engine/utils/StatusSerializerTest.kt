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
    fun `ONGOING status is deserialized back without data corruption`() =
        givenOngoingStatus()
            .serializationCycleIntroducesNoDataCorruption()

    @Test
    fun `FINISHED status with human victory is deserialized back without data corruption`() =
        givenHumanVictory()
            .serializationCycleIntroducesNoDataCorruption()

    @Test
    fun `FINISHED status with AI victory is deserialized back without data corruption`() =
        givenAiVictory()
            .serializationCycleIntroducesNoDataCorruption()

    @Test
    fun `FINISHED status with a draw is deserialized back without data corruption`() =
        givenADraw()
            .serializationCycleIntroducesNoDataCorruption()


    private fun givenHumanVictory() = FINISHED(HUMAN, 120)
    private fun givenAiVictory() = FINISHED(AI, -120)
    private fun givenADraw() = FINISHED(NOBODY, 0)
    private fun givenOngoingStatus() = ONGOING

    @UnstableDefault
    private fun Status.serializationCycleIntroducesNoDataCorruption() {
        val serialized = Json.stringify(StatusSerializer, this)
        val deserialized = Json.parse(StatusSerializer, serialized)

        assertEquals(this, deserialized)
    }
}