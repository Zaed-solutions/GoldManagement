package com.zaed.common.data.model.inventory.request

import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.inventory.InventoryType

data class FetchInventoriesRequest(
    val ownerId: String,
    val permissions: List<UserPermission> = listOf(UserPermission.SELL_PRODUCTS),
)
data class FetchInventoriesByTypeRequest(
    val ownerId: String,
    val inventoryType: InventoryType = InventoryType.PRODUCT,
)
