package com.sunnychung.lib.android.composabletable.model

import com.sunnychung.lib.android.composabletable.util.SDuration
import com.sunnychung.lib.android.composabletable.util.ZonedDateTime

interface WithDuration {
    var startAt: ZonedDateTime
    var endAt: ZonedDateTime

    fun duration(): SDuration {
        return SDuration.fromDuration(this.endAt.timestamp - this.startAt.timestamp)
    }
}
