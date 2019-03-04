package com.github.pksokolowski.nrg.dependencies

import kotlin.system.getTimeMillis

actual object TimeNow {
    actual fun get(): Long {
        return getTimeMillis()
    }
}