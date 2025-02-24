package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.UpdateWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class UpdateWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: UpdateWholesaleProductSaleRequest) = saleRepo.updateWholesaleProductSale(request)
}
