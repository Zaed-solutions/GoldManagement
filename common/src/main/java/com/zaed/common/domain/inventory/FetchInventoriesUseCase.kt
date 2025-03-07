package com.zaed.common.domain.inventory

import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.repository.InventoryRepository

class FetchInventoriesUseCase(
    private val inventoryRepo: InventoryRepository
) {
    operator fun invoke(request: FetchInventoriesRequest) = inventoryRepo.fetchInventories(request)
}