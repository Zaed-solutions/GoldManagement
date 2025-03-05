package com.zaed.common.data.source.remote

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import kotlinx.coroutines.flow.Flow

interface InventoryRemoteSource {
    fun fetchStoreInventory(request: FetchStoreInventoryRequest): Flow<Result<List<Inventory>>>
}