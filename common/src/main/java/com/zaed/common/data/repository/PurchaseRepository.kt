package com.zaed.common.data.repository

import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    suspend fun fetchPurchaseById(id: String): Result<WholesaleTransaction>
    fun fetchPurchasesUseCase(): Flow<Result<List<WholesaleTransaction>>>
    suspend fun addPurchase(request: AddPurchaseRequest): Result<String>
    suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String>
    suspend fun deletePurchase(request: DeleteWholesaleRequest): Result<Unit>
    fun fetchSupplierPurchases(request: FetchSupplierPurchasesRequest): Flow<Result<List<Purchase>>>
    fun fetchPurchases(): Flow<Result<List<Purchase>>>
}
