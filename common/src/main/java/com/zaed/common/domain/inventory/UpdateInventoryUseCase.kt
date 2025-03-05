package com.zaed.common.domain.inventory

import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import com.zaed.common.data.repository.InventoryRepository

class UpdateInventoryUseCase(
    private val inventoryRepo: InventoryRepository
) {
    suspend operator fun invoke(request: UpdateInventoryRequest) = inventoryRepo.updateInventory(request)
}