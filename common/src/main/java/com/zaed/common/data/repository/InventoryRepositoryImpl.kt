package com.zaed.common.data.repository

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import com.zaed.common.data.source.remote.InventoryRemoteSource
import kotlinx.coroutines.flow.Flow

class InventoryRepositoryImpl(
    private val remoteSource: InventoryRemoteSource
) : InventoryRepository {
    override fun fetchStoreInventory(request: FetchStoreInventoryRequest): Flow<Result<List<Inventory>>> {
        return remoteSource.fetchStoreInventory(request)
    }
}