package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddWholesaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: AddWholesaleRequest): Result<String> {
        return saleRepo.addWholesale(request)
    }
}

