package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleProductSaleRequest) =
        saleRepo.deleteWholesaleProductSale(request)
}