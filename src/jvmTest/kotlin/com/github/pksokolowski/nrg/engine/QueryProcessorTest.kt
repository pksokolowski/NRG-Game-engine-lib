package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.EngineQuery
import com.github.pksokolowski.nrg.engine.play
import com.github.pksokolowski.nrg.engine.startGame
import kotlin.test.Test
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
}