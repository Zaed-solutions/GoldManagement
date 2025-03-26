package com.zaed.common.data.model.inventory.request

import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.ui.addpurchase.ProductType

data class FetchInventoriesRequest(
    val ownerId: String,
    val permissions: List<UserPermission> = listOf(UserPermission.SELL_PRODUCTS),
    val productType: ProductType = ProductType.PRODUCT
)
