package com.sunnychung.lib.android.composabletable.demo.util

import com.sunnychung.lib.android.composabletable.demo.serializer.ZonedDateTimeSerializer
import com.sunnychung.lib.android.composabletable.demo.model.AndroidParcelable
import com.sunnychung.lib.android.composabletable.demo.model.AndroidParcelize
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable(with = ZonedDateTimeSerializer::class)
@AndroidParcelize
class ZonedDateTime(val string: String) : AndroidParcelable {
    val timestamp: Instant
    val dateTime: LocalDateTime
    val zone: String

    init {
        var match = Regex("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})([Z\\+\\-][:0-9]+)").matchEntire(string)
        val s = if (match != null) {
            "${match.groups[1]!!.value}:00${match.groups[2]!!.value}"
        } else {
            string
        }

        timestamp = Instant.parse(s)
        match = Regex("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?)([Z\\+\\-][:0-9]+)").matchEntire(s)!!
        dateTime = LocalDateTime.parse(match.groups[1]!!.value)
        zone = match.groups[2]!!.value
    }

    override fun toString(): String {
        return dateTime.toString() + zone
    }
}

fun string(char: Char, count: Int): String {
    if (count <= 0) return ""
    return (1..count).map { char }.joinToString()
}

fun prePadZero(value: Int, length: Int): String {
    val s = value.toString()
    return string('0', length - s.length) + s
}

fun LocalDateTime.ampm(): String {
    return if (this.hour < 12) {
        "am"
    } else {
        "pm"
    }
}

fun LocalDateTime.halfHour(): Int {
    return if (this.hour <= 12) {
        this.hour
    } else {
        return this.hour - 12
    }
}

fun ZonedDateTime.toDisplayText(): String {
    return "${dateTime.year}-${prePadZero(dateTime.monthNumber, 2)}-${prePadZero(dateTime.dayOfMonth, 2)} ${dateTime.halfHour()}:${prePadZero(dateTime.minute, 2)} ${dateTime.ampm()} (${zone})"
}
