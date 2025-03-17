package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.repository.PurchaseRepository
import com.zaed.common.data.repository.SaleRepository

class AddWholesaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: AddWholesaleRequest): Result<String> {
        return saleRepo.addWholesale(request)
    }
}

class AddPurchaseUseCase(
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(request: AddPurchaseRequest): Result<String> {
        return purchaseRepository.addPurchase(request)
    }
}