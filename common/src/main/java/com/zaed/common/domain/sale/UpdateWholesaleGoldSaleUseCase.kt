package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.UpdateWholesaleGoldSaleRequest
import com.zaed.common.data.repository.SaleRepository

class UpdateWholesaleGoldSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: UpdateWholesaleGoldSaleRequest) = saleRepo.updateWholesaleGoldSale(request)
}