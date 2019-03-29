package com.github.pksokolowski.nrg.engine.utils

object tallyCounter {
    var count: Long = 0
        private set

    fun click() {
        count++
    }

    fun reset(){
        count = 0
    }
}
