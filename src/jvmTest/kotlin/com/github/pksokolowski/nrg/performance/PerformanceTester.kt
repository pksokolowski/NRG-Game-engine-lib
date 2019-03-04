package com.github.pksokolowski.nrg.performance

import com.github.pksokolowski.nrg.performance.Command.*

fun main() {
    println(
        """
        Welcome to performance testing app
        ----------------------------------
        Usage:
        0 -> runs full benchmark
        3 -> runs benchmark in legacy mode, first 3 tests in this case, any number works
        0/6 -> runs test number 0 with depth 6. Any n/m works as long as test n exists
        q -> exit the app
        ----------------------------------
    """.trimIndent()
    )

    val benchmarkController =
        BenchmarkController(BenchmarkService())

    loop@ while (true) {
        print(">")
        val cmd = readLine()?.toCommand() ?: continue

        when (cmd) {
            is Benchmark -> {
                println("running performance tests suite...")
                println(benchmarkController.runBenchmark(cmd.legacyMode))
            }
            is SingleTest -> {
                println(benchmarkController.runIndividualQueries(cmd.testNum, cmd.depth))
            }
            Quit -> {
                break@loop
            }
            Unrecognized -> {
                println("you what?")
            }
        }
    }
}

sealed class Command {
    data class Benchmark(val legacyMode: Int = 0) : Command()
    data class SingleTest(val testNum: Int, val depth: Int) : Command()
    object Quit : Command()
    object Unrecognized : Command()
}

private fun String.toCommand(): Command {
    if (this == "q") return Quit
    if (this.matches("\\d+\$".toRegex())) {
        val legacyCode = this.toInt()
        return Benchmark(legacyCode)
    }
    if (this.matches("\\d+/\\d+\$".toRegex())) {
        val args = this.split("/")
        val testNum = args[0].toInt()
        val depth = args[1].toInt()
        return SingleTest(testNum, depth)
    }
    return Unrecognized
}