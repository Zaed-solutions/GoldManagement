package com.zaed.common.domain.purchase

import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.repository.PurchaseRepository

class DeletePurchaseUseCase(
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(request: DeleteWholesaleRequest) =
        purchaseRepository.deletePurchase(request)
}