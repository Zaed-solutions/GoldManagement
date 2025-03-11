package com.zaed.common.domain.manufacturerorder

import com.zaed.common.data.model.manufacturerorder.request.AddManufacturerOrderRequest
import com.zaed.common.data.repository.ManufacturerOrderRepository

class AddManufacturerOrderUseCase(
    private val repo: ManufacturerOrderRepository
) {
    suspend operator fun invoke(request: AddManufacturerOrderRequest) =
        repo.addManufacturerOrder(request)
}