package com.zaed.common.domain.purchase

import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.repository.PurchaseRepository

class FetchSupplierPurchasesUseCase(
    private val purchaseRepo: PurchaseRepository
) {
    operator fun invoke(request: FetchSupplierPurchasesRequest) = purchaseRepo.fetchSupplierPurchases(request)
}