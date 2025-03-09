package com.zaed.common.domain.sale

import com.zaed.common.data.repository.SaleRepository

class FetchAllDistributorsSalesUseCase(
    private val saleRepo: SaleRepository
) {
    operator fun invoke() = saleRepo.fetchAllDistributorsSales()
}