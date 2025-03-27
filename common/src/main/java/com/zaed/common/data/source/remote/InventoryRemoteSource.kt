package com.zaed.common.data.source.remote

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesByTypeRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import kotlinx.coroutines.flow.Flow

interface InventoryRemoteSource {
    fun fetchInventories(request: FetchInventoriesRequest): Flow<Result<List<Inventory>>>
    suspend fun addInventory(request: AddInventoryRequest): Result<Unit>
    suspend fun updateInventory(request: UpdateInventoryRequest): Result<Unit>
    fun fetchInventoriesByType(request: FetchInventoriesByTypeRequest): Flow<Result<List<Inventory>>>
}