package com.zaed.common.data.source.remote

import com.zaed.common.data.model.customer.request.FetchSupplierPurchaseRequest
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import kotlinx.coroutines.flow.Flow

interface PurchaseRemoteDataSource {
    suspend fun fetchPurchaseById(id: String): Result<WholesaleTransaction>
    suspend fun addPurchase(request: AddPurchaseRequest): Result<String>
    suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String>
    fun fetchSupplierPurchases(request: FetchSupplierPurchasesRequest): Flow<Result<List<Purchase>>>
    fun fetchPurchases(): Flow<Result<List<Purchase>>>
    suspend fun deletePurchase(request: DeleteWholesaleRequest): Result<Unit>
    fun fetchSupplierPurchase(request: FetchSupplierPurchaseRequest): Flow<Result<List<WholesaleTransaction>>>
    fun fetchPurchases(): Flow<Result<List<WholesaleTransaction>>>
    suspend fun fetchPurchase(request: FetchWholesaleRequest): Result<WholesaleTransaction>
}
