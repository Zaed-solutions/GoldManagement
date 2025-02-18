package com.zaed.common.domain

import com.zaed.common.data.model.request.FetchDistributorSalesRequest
import com.zaed.common.data.repository.SaleRepository

class FetchDistributorSalesUseCase(
    private val saleRepo: SaleRepository
) {
    operator fun invoke(request: FetchDistributorSalesRequest) = saleRepo.fetchWholesaleDistributorSales(request)
}