package com.zaed.common.domain

import com.zaed.common.data.model.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleProductSaleRequest) =
        saleRepo.deleteWholesaleProductSale(request)
}