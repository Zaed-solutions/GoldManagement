package com.zaed.common.data.model.sale

import com.zaed.common.ui.util.toMoneyFormat

data class Discount(
    val type: DiscountType = DiscountType.NONE,
    val value: Double = 0.0
){

    fun calculateDiscount(price: Double): Double {
        return when (type) {
            DiscountType.PERCENTAGE -> price * (value / 100)
            DiscountType.AMOUNT -> value
            DiscountType.NONE -> 0.0
        }
    }
    fun afterDiscount(price: Double): Double {
        return price - calculateDiscount(price)
    }
    fun toStr(): String {
        return when (type) {
            DiscountType.PERCENTAGE -> "$value%"
            DiscountType.AMOUNT -> value.toMoneyFormat(2)
            DiscountType.NONE -> "None"
        }
    }
}

