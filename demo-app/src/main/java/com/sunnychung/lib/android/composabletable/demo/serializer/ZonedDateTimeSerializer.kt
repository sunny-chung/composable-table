package com.sunnychung.lib.android.composabletable.demo.serializer

import com.sunnychung.lib.android.composabletable.demo.util.ZonedDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val s = decoder.decodeString()
        return ZonedDateTime(s)
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        val s = value.dateTime.toString() + value.zone
        encoder.encodeString(s)
    }
}
