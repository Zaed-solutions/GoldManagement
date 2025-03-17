package com.zaed.common.data.source.remote

import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import kotlinx.coroutines.flow.Flow

interface SaleRemoteSource {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreTransaction>>>
    suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String>
    suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit>
    suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit>
    suspend fun getStoreSale(saleId: String): Result<StoreTransaction>
    fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun deleteWholesale(request: DeleteWholesaleRequest): Result<Unit>
    suspend fun deleteWholesale(request: DeleteWholesaleRequest): Result<Unit>
    suspend fun fetchWholesale(request: FetchWholesaleRequest): Result<WholesaleTransaction>
    suspend fun fetchWholesale(request: FetchWholesaleRequest): Result<WholesaleTransaction>
    suspend fun addWholesale(request: AddWholesaleRequest): Result<String>
    suspend fun updateWholesale(request: UpdateWholesaleRequest): Result<Unit>
    fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun add(request: AddWholesaleRequest): Result<String>
    suspend fun updateWholesale(request: UpdateWholesaleRequest): Result<Unit>
    fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>>
    suspend fun addIngotTransaction(transaction: AddIngotTransactionRequest): Result<String>
    suspend fun updateIngotTransaction(transaction: UpdateIngotTransactionRequest): Result<Unit>
    fun fetchAllStoreSales(): Flow<Result<List<StoreTransaction>>>
    fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleTransaction>>>
}