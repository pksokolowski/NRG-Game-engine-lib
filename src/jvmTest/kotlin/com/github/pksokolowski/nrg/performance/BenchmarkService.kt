package com.github.pksokolowski.nrg.performance

import com.github.pksokolowski.nrg.engine.EngineQuery
import com.github.pksokolowski.nrg.engine.utils.toGameState

class BenchmarkService {

    val queryPreparers = listOf(
        fun(depth: Int) = """
            +1 +1 +1 +1 +1 +1 +1 +1
            +1 +1 +1 +1 +1 +1 +1 +1
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 -1 00 00 00
            -1 -1 -1 -1 00 -1 -1 -1
            -1 -1 -1 -1 -1 -1 -1 -1
        """.toQueryData(1, depth, "[realistic load] Game beginning."),

        fun(depth: Int) = """
            00 +1 00 00 00 00 00 00
            +1 00 00 00 00 00 00 00
            00 00 00 00 00 +4 00 00
            00 +3 00 00 -4 +1 00 00
            00 -2 00 00 +1 +3 00 +2
            00 00 -2 00 00 00 +3 00
            -1 -1 00 00 -3 -2 -5 -4
            00 00 -1 00 00 00 00 00
        """.toQueryData(11, depth, "Mid-game."),

        fun(depth: Int) = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 +4 00 00
            00 +3 00 +3 -4 +1 00 00
            00 -2 00 00 +1 +3 00 +2
            00 00 -2 -3 00 -2 +3 00
            -3 -3 00 00 -3 -2 -5 -4
            +5 00 -1 00 00 +5 00 +2
        """.toQueryData(11, depth, "Mid game, dense."),

        fun(depth: Int) = """
            00 00 00 00 00 00 00 00
            00 00 +3 00 00 +4 00 00
            00 +1 +1 00 +3 00 +4 +3
            00 00 +3 00 00 00 00 00
            00 -2 00 00 -3 00 00 +2
            -4 -2 -1 -3 -2 00 -2 -5
            00 00 -2 00 00 -2 +3 00
            00 00 00 00 00 00 00 00
        """.toQueryData(11, depth, "Mid game, pieces far apart."),

        fun(depth: Int) = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 +5 00 00 00 -3
            00 +4 00 00 -4 +2 00 00
            00 00 00 00 00 00 00 -2
            00 00 +2 -4 00 00 -1 00
            00 00 00 00 00 -4 00 00
        """.toQueryData(11, depth, "[realistic load] Late game."),

        fun(depth: Int) = """
            00 00 00 00 00 +3 00 00
            00 00 00 +5 00 00 00 00
            00 +4 00 00 00 +2 00 00
            00 00 00 00 00 00 00 00
            -4 00 -2 00 00 00 00 00
            00 00 00 00 00 -3 00 00
            00 00 00 -1 00 00 00 -2
            +2 00 00 -4 00 00 00 00
        """.toQueryData(11, depth, "[realistic load] Late game with pieces dispersed."),

        fun(depth: Int) = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 +2 +2 +2 +2 00 00
            00 00 +2 -4 +2 +2 00 00
            00 00 00 -2 -2 -2 00 00
            00 00 -2 -2 -2 -2 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
        """.toQueryData(21, depth, "[realistic load] Two rectangles of 2-energy pieces, zero distance."),

        fun(depth: Int) = """
            +1 00 +1 00 +1 +1 +1 00
            +1 +1 00 +1 00 +1 00 +1
            +1 00 +1 00 +1 00 +1 00
            -1 +1 00 +1 00 00 -1 00
            00 -1 00 -1 00 -1 00 -1
            -1 00 -1 00 -1 00 -1 00
            00 -1 00 -1 -1 -1 00 -1
            00 00 00 00 00 00 -1 00
        """.toQueryData(21, depth, "[realistic load] Near beginning. No captures available without sacrifice.")
    )

    class QueryData(val query: EngineQuery, val info: String)

    private fun String.toQueryData(movesCount: Int, depth: Int, description: String): QueryData {
        val state = this.toGameState(movesCount)
        val query = EngineQuery(state, depthAllowed = depth, randomize = false)
        return QueryData(query, "$description (depth = $depth)")
    }
}