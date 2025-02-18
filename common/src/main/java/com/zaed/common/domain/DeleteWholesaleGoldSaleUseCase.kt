package com.zaed.common.domain

import com.zaed.common.data.model.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteWholesaleGoldSaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleGoldSaleRequest) =
        saleRepo.deleteWholesaleGoldSale(request)
}