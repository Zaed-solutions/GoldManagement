package com.zaed.common.data.repository

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesByTypeRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import com.zaed.common.data.source.remote.InventoryRemoteSource
import kotlinx.coroutines.flow.Flow

class InventoryRepositoryImpl(
    private val remoteSource: InventoryRemoteSource
) : InventoryRepository {
    override fun fetchInventories(request: FetchInventoriesRequest): Flow<Result<List<Inventory>>> {
        return remoteSource.fetchInventories(request)
    }

    override suspend fun addInventory(request: AddInventoryRequest): Result<Unit> {
        return remoteSource.addInventory(request)
    }

    override suspend fun updateInventory(request: UpdateInventoryRequest): Result<Unit> {
        return remoteSource.updateInventory(request)
    }

    override fun fetchInventoriesByType(request: FetchInventoriesByTypeRequest): Flow<Result<List<Inventory>>> {
        return remoteSource.fetchInventoriesByType(request)
    }

}