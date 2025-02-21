package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: AddWholesaleProductSaleRequest) = saleRepo.addWholesaleProductSale(request)
}