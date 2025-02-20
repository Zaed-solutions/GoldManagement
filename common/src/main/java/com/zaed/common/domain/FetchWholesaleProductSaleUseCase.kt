package com.zaed.common.domain

import com.zaed.common.data.model.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class FetchWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: FetchWholesaleProductSaleRequest) = saleRepo.fetchWholesaleProductSale(request)
}