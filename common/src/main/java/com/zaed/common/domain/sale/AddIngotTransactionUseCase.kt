package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.repository.SaleRepository

class AddIngotTransactionUseCase(
    private val saleRepo: SaleRepository
) {
    suspend operator fun invoke(request: AddIngotTransactionRequest) = saleRepo.addIngotTransaction(request)
}