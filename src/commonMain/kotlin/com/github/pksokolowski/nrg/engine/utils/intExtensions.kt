package com.github.pksokolowski.nrg.engine.utils

import kotlin.math.max
import kotlin.math.min

internal fun Int.bound(minValue: Int, maxValue: Int) = max(minValue, min(maxValue, this))