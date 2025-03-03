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
    val discount: Discount = Discount(),
)
