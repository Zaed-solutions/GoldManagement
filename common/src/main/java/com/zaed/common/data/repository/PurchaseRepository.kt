package com.zaed.common.data.repository

import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest

interface PurchaseRepository {
    suspend fun fetchPurchaseById(id: String): Result<Purchase>
    suspend fun addPurchase(request: AddPurchaseRequest): Result<String>
    suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String>
}
