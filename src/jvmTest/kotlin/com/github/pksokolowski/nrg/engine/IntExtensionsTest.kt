package com.github.pksokolowski.nrg.engine

import com.github.pksokolowski.nrg.engine.utils.bound
import kotlin.test.Test
import kotlin.test.assertEquals

class IntExtensionsTest{
    @Test
    fun `bound works on lower bound`(){
        val input = -5
        val absBound = 4

        val result = input.bound(-absBound, absBound)
        assertEquals(-4, result)
    }

    @Test
    fun `input beyond max value is cut correctly`(){
        val input = 5
        val absBound = 4

        val result = input.bound(-absBound, absBound)
        assertEquals(4, result)
    }
}