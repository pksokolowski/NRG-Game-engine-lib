package com.github.pksokolowski.nrg.performance

import com.github.pksokolowski.nrg.engine.EngineQuery
import com.github.pksokolowski.nrg.engine.Move
import com.github.pksokolowski.nrg.engine.utils.toGameState

class BenchmarkService {

    val queryPreparers = listOf(
        ::prepareGameStartQuery,
        ::prepareMidGameQuery,
        ::prepareMidGameDenseQuery,
        ::prepareMidGameFarApartQuery,
        ::prepareLateGameQuery,
        ::prepareDispersedLateGameQuery,
        ::prepareBattleOf2sQuery,
        ::prepareClinchCase
    )

    class QueryData(val query: EngineQuery, val info: String)

    fun prepareGameStartQuery(depth: Int): QueryData {
        val state = """
            +1 +1 +1 +1 +1 +1 +1 +1
            +1 +1 +1 +1 +1 +1 +1 +1
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            -1 -1 -1 -1 -1 -1 -1 -1
            -1 -1 -1 -1 -1 -1 -1 -1
        """.toGameState()

        val move = Move(-1, 4, 6, 4, 5, 0)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "[realistic load] Game beginning. (depth = $depth)"
        )
    }

    fun prepareMidGameQuery(depth: Int): QueryData {
        val state = """
            00 +1 00 00 00 00 00 00
            +1 00 00 00 00 00 00 00
            00 00 00 00 00 +4 00 00
            00 +3 00 00 +2 +1 00 00
            00 -2 00 -3 +1 +3 00 +2
            00 00 -2 00 00 00 +3 00
            -1 -1 00 00 -3 -2 -5 -4
            00 00 -1 00 00 00 00 00
        """.toGameState(10)

        val move = Move(-3, 3, 4, 4, 3, 2)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(query, "Mid-game. (depth = $depth)")
    }

    fun prepareMidGameDenseQuery(depth: Int): QueryData {
        val state = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 +4 00 00
            00 +3 00 +3 +2 +1 00 00
            00 -2 00 -3 +1 +3 00 +2
            00 00 -2 -3 00 -2 +3 00
            -3 -3 00 00 -3 -2 -5 -4
            +5 00 -1 00 00 +5 00 +2
        """.toGameState(10)

        val move = Move(-3, 3, 4, 4, 3, 2)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "Mid game, dense. (depth = $depth)"
        )
    }

    fun prepareMidGameFarApartQuery(depth: Int): QueryData {
        val state = """
            00 00 00 00 00 00 00 00
            00 00 +3 00 00 +4 00 00
            00 +1 +1 00 +3 00 +4 +3
            00 00 +3 00 00 00 00 00
            00 -2 00 00 +1 00 00 +2
            -4 -2 -1 -3 -2 -2 -2 -5
            00 00 -2 00 00 -2 +3 00
            00 00 00 00 00 00 00 00
        """.toGameState(10)

        val move = Move(-2, 5, 5, 4, 4, 1)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "Mid game, pieces far apart. (depth = $depth)"
        )
    }

    fun prepareLateGameQuery(depth: Int): QueryData {
        val state = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 00 +5 00 00 00 -3
            00 +4 00 00 +3 +2 00 00
            00 00 00 -2 00 00 00 -2
            00 00 +2 -4 00 00 -1 00
            00 00 00 00 00 -4 00 00
        """.toGameState(10)

        val move = Move(-2, 3, 5, 4, 4, 3)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "[realistic load] Late game (depth = $depth)"
        )
    }

    fun prepareDispersedLateGameQuery(depth: Int): QueryData {
        val state = """
            00 00 00 00 00 +3 00 00
            00 00 00 +5 00 00 00 00
            00 +4 00 00 00 +2 00 00
            00 00 00 00 00 00 00 00
            -4 00 -2 00 00 00 00 00
            00 00 00 00 00 -3 00 00
            00 00 00 -1 00 00 00 -2
            +2 00 00 00 00 -4 00 00
        """.toGameState(10)

        val move = Move(-4, 5, 7, 3, 7, 0)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "[realistic load] Late game with pieces dispersed (depth = $depth)"
        )
    }

    fun prepareBattleOf2sQuery(depth: Int): QueryData {
        val state = """
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
            00 00 +2 +2 +2 +2 00 00
            00 00 +2 +2 +2 +2 00 00
            00 00 -2 -2 -2 -2 00 00
            00 00 -2 -2 -2 -2 00 00
            00 00 00 00 00 00 00 00
            00 00 00 00 00 00 00 00
        """.toGameState(20)

        val move = Move(-2, 2, 4, 3, 3, 2)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "[realistic load] Two rectangles of 2-energy pieces, zero distance (depth = $depth)"
        )
    }

    fun prepareClinchCase(depth: Int): QueryData {
        val state = """
            +1 00 +1 00 +1 +1 +1 00
            +1 +1 00 +1 00 +1 00 +1
            +1 00 +1 00 +1 00 +1 00
            -1 +1 00 +1 00 00 -1 00
            00 -1 00 -1 00 -1 00 -1
            -1 00 -1 00 -1 00 -1 00
            00 -1 00 -1 00 -1 00 -1
            00 00 00 00 -1 00 -1 00
        """.toGameState(20)

        val move = Move(-1, 4, 7, 4, 6, 2)
        val query = EngineQuery(state, move, depthAllowed = depth, randomize = false)
        return QueryData(
            query,
            "[realistic load] Near beginning. No captures available without sacrifice (depth = $depth)"
        )
    }
}