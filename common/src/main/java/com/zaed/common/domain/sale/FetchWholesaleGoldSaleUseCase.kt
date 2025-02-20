package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.repository.SaleRepository

class FetchWholesaleGoldSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: FetchWholesaleGoldSaleRequest) = saleRepo.fetchWholesaleGoldSale(request)
}