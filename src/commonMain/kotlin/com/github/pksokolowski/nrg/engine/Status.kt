package com.github.pksokolowski.nrg.engine

sealed class Status {
    object ONGOING : Status()
    data class FINISHED(val winner: Player, val eval: Int) : Status() {
        enum class Player {
            HUMAN,
            AI,
            NOBODY
        }
    }
}