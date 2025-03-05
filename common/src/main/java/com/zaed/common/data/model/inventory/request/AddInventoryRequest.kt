package com.zaed.common.data.model.inventory.request

import com.zaed.common.data.model.inventory.Inventory

data class AddInventoryRequest(
    val mainInventoryId: String,
    val inventory: Inventory
)
