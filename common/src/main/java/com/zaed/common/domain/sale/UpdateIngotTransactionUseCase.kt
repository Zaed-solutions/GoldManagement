package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.repository.SaleRepository

class UpdateIngotTransactionUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: UpdateIngotTransactionRequest) = saleRepo.updateIngotTransaction(request)
}