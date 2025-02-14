package com.zaed.common.domain

import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddStoreSaleUseCase(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(request: AddStoreSaleRequest) = saleRepository.addStoreSale(request)
}