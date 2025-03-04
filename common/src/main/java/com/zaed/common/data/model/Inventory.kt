package com.zaed.common.data.model

import com.zaed.common.data.model.sale.Karat
import java.util.Date

data class Inventory(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val quantity: Double = 0.0,
    val quantityUnit: QuantityUnit = QuantityUnit.GRAMS,
    val lastUpdated: Date = Date(),
    val type: InventoryType,
    val karat: String = "",
)

enum class QuantityUnit {
    GRAMS,
    UNITS
}

enum class InventoryType {
    PRODUCT,
    GOLD,
    INGOT
}
