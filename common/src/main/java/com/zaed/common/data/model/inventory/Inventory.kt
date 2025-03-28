package com.zaed.common.data.model.inventory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R
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
    val buyingPrice: Double = 0.0,
)

enum class QuantityUnit {
    GRAMS,
    UNITS
}

enum class InventoryType(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
) {
    PRODUCT(
        R.string.products,
        R.drawable.ic_cart
    ),
    GOLD(
        R.string.gold,
        R.drawable.ic_gold
    ),
    SILVER(
        R.string.silver,
        R.drawable.ic_ingot
    ),
    INGOT(
        R.string.ingots,
        R.drawable.ic_ingot
    )
}

fun UserPermission.toInventoryType(): InventoryType {
    return when (this) {
        UserPermission.SELL_PRODUCTS -> InventoryType.PRODUCT
        UserPermission.SELL_GOLD -> InventoryType.GOLD
        UserPermission.SELL_INGOTS -> InventoryType.INGOT
        else -> InventoryType.PRODUCT
    }
}
