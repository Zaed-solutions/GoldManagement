package com.zaed.common.domain.inventory

import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.repository.InventoryRepository

class AddInventoryUseCase(
    private val inventoryRepo: InventoryRepository
) {
    suspend operator fun invoke(request: AddInventoryRequest) = inventoryRepo.addInventory(request)
}