package com.github.pksokolowski.nrg.dependencies

import java.util.*

actual object TimeNow {
    actual fun get(): Long {
        return Calendar.getInstance().timeInMillis
    }
}