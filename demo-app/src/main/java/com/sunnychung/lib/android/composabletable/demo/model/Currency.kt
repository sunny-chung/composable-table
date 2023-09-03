package com.sunnychung.lib.android.composabletable.demo.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val unit: String?,
    val amount: Double
) {
    operator fun plus(other: Currency): Currency {
        if (other.unit != unit && other.unit != null && unit != null) {
            throw UnsupportedOperationException("Currencies of different unit cannot be calculated")
        }
        return Currency(unit ?: other.unit, amount + other.amount)
    }

    override fun toString(): String {
        return (unit ?: "") + amount
    }
}
