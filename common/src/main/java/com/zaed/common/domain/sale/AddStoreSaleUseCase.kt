package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddStoreSaleUseCase(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(request: AddStoreSaleRequest) = saleRepository.addStoreSale(request)
}