package com.sunnychung.lib.android.composabletable.ux

import com.sunnychung.lib.android.composabletable.model.TransitConnect
import com.sunnychung.lib.android.composabletable.util.SDuration
import com.sunnychung.lib.android.composabletable.util.ZonedDateTime
import com.sunnychung.lib.android.composabletable.util.ampm
import com.sunnychung.lib.android.composabletable.util.halfHour
import com.sunnychung.lib.android.composabletable.util.prePadZero

fun TransitConnect.Summary.duration(): SDuration {
    return SDuration.fromDuration(this.endAt.timestamp - this.startAt.timestamp)
}

fun SDuration.formatHoursMins(): String {
    if (this.hoursPart == 0) {
        return "${this.minutesPart}m"
    }
    return "${this.hoursPart}h ${this.minutesPart}m"
}

fun ZonedDateTime.formatHalfHoursMins(): String {
    return "${dateTime.halfHour()}:${prePadZero(dateTime.minute, 2)} ${dateTime.ampm()} (${zone})"
}

fun ZonedDateTime.formatShortHoursMins(): String {
    return "${prePadZero(dateTime.hour, 2)}:${prePadZero(dateTime.minute, 2)}"
}

fun TransitConnect.keyStopsFormatted(): String {
    return this.keyStops
        .map { it.name }
        .joinToString(" → ")
}

fun TransitConnect.PublicTransit.name(): String {
    return listOf(this.operator, this.line, this.direction, this.speed)
        .filter { !it.isNullOrBlank() }
        .joinToString(" ")
}

fun TransitConnect.PublicTransit.departureFormatted(): String? {
    return listOf(srcPlatform.letIfNotEmpty { "Platform $it" }, srcExtraInfo)
        .filter { !it.isNullOrBlank() }
        .joinToString(", ")
        .emptyToNull()
}

fun TransitConnect.PublicTransit.arrivalFormatted(): String? {
    return listOf(destPlatform.letIfNotEmpty { "Platform $it" }, destExtraInfo)
        .filter { !it.isNullOrBlank() }
        .joinToString(", ")
        .emptyToNull()
}

fun String.emptyToNull(): String? {
    if (isEmpty()) return null
    return this
}

fun <T> String?.letIfNotEmpty(mapping: (String) -> T): T? {
    if (this.isNullOrEmpty()) return null
    return mapping(this)
}
