package com.github.pksokolowski.nrg.dependencies

import kotlin.js.Date

actual object TimeNow {
    actual fun get(): Long {
        return Date.now().toLong()
    }
}