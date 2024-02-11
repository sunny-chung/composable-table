package com.sunnychung.lib.android.composabletable.demo.model

import com.sunnychung.lib.android.composabletable.demo.util.ZonedDateTime
import kotlinx.serialization.Serializable

@Serializable
@AndroidParcelize
class TransitConnect : AndroidParcelable {
    lateinit var summary: Summary
    var keyStops = listOf<KeyStop>()
    var connects: MutableList<Connect> = mutableListOf()

    var prevScheduleUrl: String? = null
    var nextScheduleUrl: String? = null

    var code: String? = null

    @Serializable
    @AndroidParcelize
    class Summary : WithDuration, AndroidParcelable {
        override lateinit var startAt: ZonedDateTime
        override lateinit var endAt: ZonedDateTime
        var fares: MutableMap<String, Currency> = mutableMapOf()
        lateinit var totalFare: Currency
        var walkingSeconds: Long = -1
        var waitingSeconds: Long = -1
        var numOfTrips: Int = -1
    }

    @Serializable
    @AndroidParcelize
    class KeyStop : AndroidParcelable {
        lateinit var name: String
        var type: TransitType? = null
//        var location: Location? = null
        var surroundMapUrl: String? = null
        var inStopMapUrl: String? = null
        var timetableUrls: List<Pair<String, String>> = mutableListOf() // list([Provider Name, URL])

//        var busStops: List<BusStopPayload> = mutableListOf()
//        var railwayStation: RailwayStationPayload? = null

//        var itineraryRemark: String? = null
    }

    @Serializable
    @AndroidParcelize
    class Connect : WithDuration, AndroidParcelable {
        lateinit var connectType: ConnectType
        override lateinit var startAt: ZonedDateTime
        override lateinit var endAt: ZonedDateTime
        lateinit var startAtStop: KeyStop
        lateinit var endAtStop: KeyStop
        var transit: PublicTransit? = null
    }

    @Serializable
    @AndroidParcelize
    class PublicTransit : AndroidParcelable {
        lateinit var type: TransitType
        var operator: String? = null
        var line: String? = null
        var direction: String? = null
        var speed: String? = null

        var srcPlatform: String? = null
        var srcExtraInfo: String? = null
        var destPlatform: String? = null
        var destExtraInfo: String? = null

        lateinit var baseFare: Currency
        var extraFees: MutableList<ChargableItem> = mutableListOf()
        var isReservationMandatory: Boolean? = null

        var intermediateStops: MutableList<IntermediateStop> = mutableListOf()

        var facilities: MutableList<String> = mutableListOf()
    }

    enum class ConnectType {
        Walk, PublicTransit
    }

    enum class TransitType {
        JR, Bus, Subway, Ferry, Flight, Others, Poi
    }

    @Serializable
    @AndroidParcelize
    class ChargableItem : AndroidParcelable {
        lateinit var item: String
        lateinit var fee: Currency
        var isMandatory: Boolean = false
    }

    @Serializable
    @AndroidParcelize
    class IntermediateStop : AndroidParcelable {
        lateinit var name: String
        var arrivalTime: ZonedDateTime? = null
//        var location: Location? = null
    }

}
