package com.krzysztofsroga.librehome.utils

import kotlin.time.ClockMark
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

fun Int.isEven() = (this and 1) == 0
fun Int.odd() = (this and 1) == 1