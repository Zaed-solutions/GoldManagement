package com.zaed.common.data.repository

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun fetchStoreInventory(request: FetchStoreInventoryRequest): Flow<Result<List<Inventory>>>
}