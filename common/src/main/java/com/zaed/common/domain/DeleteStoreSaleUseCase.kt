package com.zaed.common.domain

import com.zaed.common.data.model.request.DeleteStoreSaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteStoreSaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: DeleteStoreSaleRequest) = saleRepo.deleteStoreSale(request)
}