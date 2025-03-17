package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.repository.SaleRepository

class DeleteWholesaleUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleRequest) =
        saleRepo.deleteWholesale(request)
}