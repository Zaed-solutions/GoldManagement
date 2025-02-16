package com.zaed.common.domain

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.repository.SaleRepository

class GetStoreSaleUseCase(
    private val storeSaleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: String): Result<StoreSale>  {
        return storeSaleRepository.getStoreSale(saleId)
    }
}