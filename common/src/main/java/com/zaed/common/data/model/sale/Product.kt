package com.zaed.common.data.model.sale

data class Product(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 1,
    val gramPrice: Double = 0.0,
    val minPrice: Double = 0.0,
    val categoryId: String = "",
    val laborCost: Double = 0.0,
    val grams: Double = 0.0,
    val karat: Karat = Karat.K18,
    val discount: Discount = Discount(),
    val buyingPrice: Double = 0.0,
){
    val priceForItem get() = grams*gramPrice
    val totalLaborCost get() = grams*laborCost
    val totalPriceBeforeDiscount get() = (quantity)*(priceForItem+ totalLaborCost)
    val discountAmount get() = discount.calculateDiscount(totalPriceBeforeDiscount)
    val discountAsStr get() = discount.toStr()
    val totalPriceAfterDiscount get() = totalPriceBeforeDiscount - discount.calculateDiscount(totalPriceBeforeDiscount)
}
