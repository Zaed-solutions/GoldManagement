package com.zaed.common.data.model.inventory

import com.zaed.common.data.model.authentication.UserPermission
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
    val type: InventoryType = InventoryType.PRODUCT,
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

fun UserPermission.toInventoryType(): InventoryType {
    return when (this) {
        UserPermission.SELL_PRODUCTS -> InventoryType.PRODUCT
        UserPermission.SELL_GOLD -> InventoryType.GOLD
        UserPermission.SELL_INGOTS -> InventoryType.INGOT
        else -> InventoryType.PRODUCT
    }
}
