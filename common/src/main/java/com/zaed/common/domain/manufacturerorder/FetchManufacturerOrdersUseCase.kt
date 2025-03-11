package com.zaed.common.domain.manufacturerorder

import com.zaed.common.data.repository.ManufacturerOrderRepository

class FetchManufacturerOrdersUseCase(
    private val repo: ManufacturerOrderRepository
) {
    operator fun invoke() = repo.fetchManufacturerOrders()
}