package com.zaed.common.data.model.sale

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
}

