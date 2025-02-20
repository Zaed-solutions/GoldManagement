package com.zaed.common.data.model.sale

data class Discount(
    val type: DiscountType = DiscountType.NONE,
    val value: Double = 0.0
)

