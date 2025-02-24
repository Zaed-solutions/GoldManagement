package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.repository.SaleRepository

class FetchIngotTransactionsUseCase(
    private val saleRepo: SaleRepository
) {
    operator fun invoke(request: FetchIngotTransactionsRequest) = saleRepo.fetchIngotTransaction(request)
}