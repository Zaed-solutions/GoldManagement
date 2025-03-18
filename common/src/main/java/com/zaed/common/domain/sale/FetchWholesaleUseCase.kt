package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.repository.SaleRepository

class FetchWholesaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: FetchWholesaleRequest) = saleRepo.fetchWholesale(request)
}
