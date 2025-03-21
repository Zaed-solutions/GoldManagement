package com.zaed.common.data.model.category

import com.zaed.common.data.model.inventory.Inventory

data class Category(
    val id: String = "",
    val name: String = "",
    val availableGrams: Double = 0.0,
)

fun Inventory.toCategory(): Category {
    return Category(
        id = productId,
        name = productName,
        availableGrams = quantity
    )
}