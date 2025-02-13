package com.zaed.common.data.model

data class Discount(
    val type: DiscountType = DiscountType.NONE,
    val value: Double = 0.0
)

enum class DiscountType {
    NONE,
    PERCENTAGE,
    AMOUNT
}
