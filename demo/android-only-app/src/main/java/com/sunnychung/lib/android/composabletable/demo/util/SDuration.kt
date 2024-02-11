package com.sunnychung.lib.android.composabletable.demo.util

import kotlin.time.Duration

class SDuration {
    val totalMs: Long

    val dayPart: Int
    val hoursPart: Int
    val minutesPart: Int
    val secondsPart: Int
    val msPart: Int

    constructor(totalMs: Long) {
        this.totalMs = totalMs

        this.msPart = (totalMs % 1000L).toInt()
        this.secondsPart = ((totalMs / 1000L) % 60L).toInt()
        this.minutesPart = ((totalMs / 1000L / 60L) % 60L).toInt()
        this.hoursPart = ((totalMs / 1000L / 60L / 60L) % 24L).toInt()
        this.dayPart = (totalMs / 1000L / 60L / 60L / 24L).toInt()
    }

    fun minutes(): Long {
        return totalMs / 1000L / 60L
    }

    companion object {
        fun fromDuration(duration: Duration) = SDuration(duration.inWholeMilliseconds)
    }
}
