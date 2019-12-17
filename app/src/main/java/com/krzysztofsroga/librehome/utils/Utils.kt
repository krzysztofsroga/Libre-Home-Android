package com.krzysztofsroga.librehome.utils

import kotlin.time.ClockMark
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

@UseExperimental(ExperimentalTime::class)
object FunctionTimer {

    private val lastCallTable: MutableMap<Function<*>, ClockMark> = mutableMapOf() //concurrent map instead of synchronized?

    fun <T> ((T) -> Unit).throttle(maxCallEvery: Duration): (T) -> Unit { //This function shouldn't be called on UI thread!
        synchronized(FunctionTimer) {
            val lastCall = lastCallTable[this]
            if (lastCall != null && lastCall.elapsedNow() < maxCallEvery) {
                return {}
            }
            lastCallTable[this] = MonoClock.markNow() //getorput
            return { this(it) }
        }
    }
}


fun Int.isEven() = (this and 1) == 0
fun Int.odd() = (this and 1) == 1