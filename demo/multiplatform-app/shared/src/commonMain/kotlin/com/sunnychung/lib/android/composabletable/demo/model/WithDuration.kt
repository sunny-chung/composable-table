package com.sunnychung.lib.android.composabletable.demo.model

import com.sunnychung.lib.android.composabletable.demo.util.SDuration
import com.sunnychung.lib.android.composabletable.demo.util.ZonedDateTime

interface WithDuration {
    var startAt: ZonedDateTime
    var endAt: ZonedDateTime

    fun duration(): SDuration {
        return SDuration.fromDuration(this.endAt.timestamp - this.startAt.timestamp)
    }
}
