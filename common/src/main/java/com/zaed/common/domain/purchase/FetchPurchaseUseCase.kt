package com.zaed.common.domain.purchase

import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.repository.PurchaseRepository

class FetchPurchaseUseCase(
    private val purchaseRepo: PurchaseRepository
) {
    suspend operator fun invoke(id: String): Result<WholesaleTransaction> = purchaseRepo.fetchPurchaseById(id)
}
