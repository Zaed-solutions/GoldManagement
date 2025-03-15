package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.repository.SaleRepository

class GetStoreSaleUseCase(
    private val storeSaleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: String): Result<StoreTransaction>  {
        return storeSaleRepository.getStoreSale(saleId)
    }
}