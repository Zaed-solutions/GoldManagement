package com.zaed.common.domain.purchase

import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.repository.PurchaseRepository

class FetchPurchaseUseCase(
    private val purchaseRepo: PurchaseRepository
) {
    suspend operator fun invoke(id: String): Result<Purchase> = purchaseRepo.fetchPurchaseById(id)
}