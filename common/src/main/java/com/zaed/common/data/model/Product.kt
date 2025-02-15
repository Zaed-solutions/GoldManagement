package com.zaed.common.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 1,
    val gramPrice: Double = 0.0,
    val minPrice: Double = 0.0,
    val categoryId: String = "",
    val grams: Double = 1.0,
)
