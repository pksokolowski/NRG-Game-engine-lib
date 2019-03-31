package com.github.pksokolowski.nrg.dependencies

actual object TimeNow {
    actual fun get(): Long {
        return System.currentTimeMillis()
    }
}