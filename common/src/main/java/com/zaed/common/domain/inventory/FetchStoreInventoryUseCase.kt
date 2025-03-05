package com.zaed.common.domain.inventory

import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import com.zaed.common.data.repository.InventoryRepository

class FetchStoreInventoryUseCase(
    private val inventoryRepo: InventoryRepository
) {
    operator fun invoke(request: FetchStoreInventoryRequest) = inventoryRepo.fetchStoreInventory(request)
}