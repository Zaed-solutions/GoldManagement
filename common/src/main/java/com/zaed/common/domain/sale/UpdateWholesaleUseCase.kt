package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import com.zaed.common.data.repository.SaleRepository

class UpdateWholesaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: UpdateWholesaleRequest) = saleRepo.updateWholesale(request)
}
