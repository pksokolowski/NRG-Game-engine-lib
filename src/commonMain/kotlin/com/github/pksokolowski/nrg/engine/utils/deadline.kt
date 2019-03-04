package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.dependencies.TimeNow

fun getDeadline(timeFromNow: Long?) = timeFromNow?.let { now() + timeFromNow }

fun isDeadlineCrossed(deadline: Long?) = deadline?.let { now() > it } ?: false

private fun now() = TimeNow.get()