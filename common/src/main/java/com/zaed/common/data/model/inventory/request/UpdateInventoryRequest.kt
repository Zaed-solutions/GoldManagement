package com.zaed.common.data.model.inventory.request

data class UpdateInventoryRequest(
    val mainInventoryId: String,
    val inventoryId: String,
    val quantity: Double
)