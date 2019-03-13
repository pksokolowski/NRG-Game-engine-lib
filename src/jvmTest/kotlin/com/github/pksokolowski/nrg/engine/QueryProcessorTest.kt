package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.Status.FINISHED.Player.AI
import com.github.pksokolowski.nrg.engine.Status.FINISHED.Player.HUMAN
import com.github.pksokolowski.nrg.engine.utils.toGameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class QueryProcessorTest{
    @Test
    fun `basic flow scenario`(){
        val initialResponse = startGame()
        val chosenMove = initialResponse.possibleMoves[0]

        val secondQuery = EngineQuery(initialResponse.state, chosenMove, depthAllowed = 3)
        val secondResponse = play(secondQuery)

        if(secondResponse.state[chosenMove.x1, chosenMove.y1] != 0) fail()
    }

    @Test
    fun `human winner is detected correctly`(){
        val state = """
            00 +1 00
            00 00 -1
        """.toGameState()
        val chosenMove = Move(-1, 2, 1, 1, 0, -1)

        val query = EngineQuery(state, chosenMove, depthAllowed = 3)
        val response = play(query)

        val finished = response.status as Status.FINISHED
        assertEquals(HUMAN, finished.winner)
    }

    @Test
    fun `AI winner is detected correctly`(){
        val state = """
            +4 +1 00
            00 00 -1
        """.toGameState()
        val chosenMove = Move(-1, 2, 1, 1, 0, -1)

        val query = EngineQuery(state, chosenMove, depthAllowed = 3)
        val response = play(query)

        val finished = response.status as Status.FINISHED
        assertEquals(AI, finished.winner)
    }
}