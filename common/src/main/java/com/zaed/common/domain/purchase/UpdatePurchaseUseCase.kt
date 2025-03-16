package com.zaed.common.domain.purchase

import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.data.repository.PurchaseRepository

class UpdatePurchaseUseCase(
    private val purchaseRepo: PurchaseRepository
) {
    suspend operator fun invoke(request: UpdatePurchaseRequest) = purchaseRepo.updatePurchase(request)
}