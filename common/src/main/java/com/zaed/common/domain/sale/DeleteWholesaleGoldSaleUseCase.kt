package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteWholesaleGoldSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleGoldSaleRequest) =
        saleRepo.deleteWholesaleGoldSale(request)
}