package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class FetchWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: FetchWholesaleProductSaleRequest) = saleRepo.fetchWholesaleProductSale(request)
}
