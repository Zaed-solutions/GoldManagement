package com.zaed.common.domain.purchase

import com.zaed.common.data.repository.PurchaseRepository

class FetchPurchasesUseCase(
    private val purchaseRepo: PurchaseRepository
) {
    operator fun invoke() = purchaseRepo.fetchPurchases()
}