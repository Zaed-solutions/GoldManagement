package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddWholesaleGoldSaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddGoldSaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: AddWholesaleGoldSaleRequest): Result<String> {
        return saleRepo.addGoldSale(request)
    }
}