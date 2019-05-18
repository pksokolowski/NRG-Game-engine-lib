package com.github.pksokolowski.nrg.experiments

import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.MIN_SCORE
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() {
    val numWorkers = 4
    var bestScore = MIN_SCORE
    var bestMove: Move? = null

    runBlocking {
        val time = measureTimeMillis {

            val workers = List(numWorkers) {
                async(Dispatchers.Default) {
                    val score = wasteTime()
                    if (score > bestScore) {
                        bestMove = null
                        bestScore = score
                    }
                }
            }

            workers.awaitAll()

        }
        println("""
            completed in $time
            best score = $bestScore
            best move = $bestMove
        """.trimIndent())
    }
}

fun wasteTime(candidate: Int = 300_000_000): Int {
    var dividers = 0
    for (i in 1 until candidate)
        if (candidate % i == 0) dividers++
    return dividers
}