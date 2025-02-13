package com.zaed.common.domain

import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.data.repository.SaleRepository

class FetchStoreSalesUseCase(
    private val saleRepository: SaleRepository
) {
    operator fun invoke(request: FetchStoreSalesRequest) = saleRepository.fetchStoreSales(request)
}