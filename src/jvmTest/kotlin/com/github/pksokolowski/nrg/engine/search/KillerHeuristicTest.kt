package com.github.pksokolowski.nrg.engine.search

import com.github.pksokolowski.nrg.engine.Move
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KillerHeuristicTest{
    @Test
    fun `adds new moves if not there already`(){
        val killer = KillerHeuristic(5)
        val moveA = Move(1, 0,0,1,1,0)

        killer.remember(moveA, 1)
        val recalled = killer at 1

        assertEquals(moveA, recalled.first())
    }

    @Test
    fun `does not accept the same move twice`(){
        val killer = KillerHeuristic(5)
        val moveA = Move(1, 0,0,1,1,0)
        val moveB = Move(1, 1,2,0,1,0)

        killer.remember(moveA, 1)
        killer.remember(moveB, 1)
        killer.remember(moveB, 1)

        val recalled = killer at 1
        val expected = listOf(moveA, moveB)

        assertEquals(expected, recalled)
    }

    @Test
    fun `handles multiple moves added to same depth (replaces old)`(){
        val killer = KillerHeuristic(5)
        val moveA = Move(1, 0,0,1,1,0)
        val moveB = Move(1, 1,2,0,1,0)
        val moveC = Move(2, 2,2,0,1,0)

        killer.remember(moveA, 1)
        killer.remember(moveB, 1)
        killer.remember(moveC, 1)

        val recalled = killer at 1
        val expected = listOf(moveC, moveB)

        assertEquals(expected, recalled)
    }

    @Test
    fun `ignores captures`(){
        val killer = KillerHeuristic(5)
        val move = Move(1, 0,0,1,1,-1)

        killer.remember(move, 1)
        val recalled = killer at 1

        assertTrue{recalled.isEmpty()}
    }
}