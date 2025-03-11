package com.zaed.common.domain.manufacturerorder

import com.zaed.common.data.model.manufacturerorder.request.DeleteManufacturerOrderRequest
import com.zaed.common.data.repository.ManufacturerOrderRepository

class DeleteManufacturerOrderUseCase(
    private val repo: ManufacturerOrderRepository
) {
    suspend operator fun invoke(request: DeleteManufacturerOrderRequest) =
        repo.deleteManufacturerOrder(request)
}