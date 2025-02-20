package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.repository.SaleRepository

class FetchDistributorSalesUseCase(
    private val saleRepo: SaleRepository
) {
    operator fun invoke(request: FetchDistributorSalesRequest) = saleRepo.fetchWholesaleDistributorSales(request)
}