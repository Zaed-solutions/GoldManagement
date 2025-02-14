package com.zaed.common.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 1,
    val minPrice: Double = 0.0,
    val price: Double = 0.0,
    val category: Category = Category(),
    val grams: Int = 0,
)
