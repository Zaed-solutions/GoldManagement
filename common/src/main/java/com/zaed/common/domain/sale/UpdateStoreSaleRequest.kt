package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.repository.SaleRepository

class UpdateStoreSaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: UpdateStoreSaleRequest) = saleRepo.updateStoreSale(request)
}