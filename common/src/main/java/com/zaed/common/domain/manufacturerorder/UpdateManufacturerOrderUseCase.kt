package com.zaed.common.domain.manufacturerorder

import com.zaed.common.data.model.manufacturerorder.request.UpdateManufacturerOrderRequest
import com.zaed.common.data.repository.ManufacturerOrderRepository

class UpdateManufacturerOrderUseCase(
    private val repo: ManufacturerOrderRepository
) {
    suspend operator fun invoke(request: UpdateManufacturerOrderRequest) =
        repo.updateManufacturerOrder(request)
}