package com.zaed.common.domain.purchase

import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.repository.PurchaseRepository

class AddPurchaseUseCase(
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(request: AddPurchaseRequest): Result<String> {
        return purchaseRepository.addPurchase(request)
    }
}