package com.github.pksokolowski.nrg.performance

import com.github.pksokolowski.nrg.performance.BenchmarkService.QueryData
import com.github.pksokolowski.nrg.engine.play
import java.lang.StringBuilder
import kotlin.system.measureTimeMillis


class BenchmarkController(private val benchmarkService: BenchmarkService) {

    fun runIndividualQueries(type: Int, depth: Int): String {
        require(type < benchmarkService.queryPreparers.size) {
            return """
                Test doesn't exist. There are only ${benchmarkService.queryPreparers.size} tests.
                Type 0/5 to run the first one with depth 5
            """.trimIndent()
        }
        val queryData = benchmarkService.queryPreparers[type].invoke(depth)
        val time = measureTimeMillis {
            val response = play(queryData.query)
        }
        return "${queryData.info} took $time ms"
    }

    fun runBenchmark(untilType: Int = 0): String {
        var totalTime = 0L
        val partialResults = mutableListOf<String>()
        val queriesRan = mutableListOf<QueryData>()

        val depths = listOf(8, 7, 6, 6, 8, 8, 9)

        for (i in benchmarkService.queryPreparers.indices) {
            if (untilType > 0 && i == untilType) break
            val queryData = benchmarkService.queryPreparers[i](depths[i])
            queriesRan.add(queryData)

            val time = measureTimeMillis {
                val response = play(queryData.query)
            }
            totalTime += time
            partialResults.add("${queryData.info} took $time ms")
        }


        val sb = StringBuilder()
        sb.appendln("Total time: $totalTime ms")

        val benchmarkLegacyMapping = "${queriesRan.size}"
        sb.appendln(
            """
            This was equvalent to command: $benchmarkLegacyMapping

            Tests ran:
        """.trimIndent()
        )

        for ((i, it) in partialResults.withIndex()) {
            val depth = queriesRan[i].query.depthAllowed
            sb.append("$i/$depth : $it \n")
        }

        return sb.toString()
    }
}