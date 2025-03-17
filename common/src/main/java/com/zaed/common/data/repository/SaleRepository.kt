package com.zaed.common.data.repository

import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreTransaction>>>
    suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String>
    suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit>
    suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit>
    suspend fun getStoreSale(saleId: String): Result<StoreTransaction>
    fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit>
    suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleRequest): Result<Unit>
    suspend fun fetchWholesaleProductSale(request: FetchWholesaleProductSaleRequest): Result<WholesaleTransaction>
    suspend fun fetchWholesaleGoldSale(request: FetchWholesaleGoldSaleRequest): Result<WholesaleTransaction>
    fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun addWholesaleProductSale(request: AddWholesaleRequest): Result<String>
    suspend fun addGoldSale(request: AddWholesaleRequest): Result<String>
    suspend fun updateWholesaleProductSale(request: UpdateWholesaleRequest): Result<Unit>
    suspend fun updateWholesaleGoldSale(request: UpdateWholesaleRequest): Result<Unit>
    fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>>
    suspend fun addIngotTransaction(transaction: AddIngotTransactionRequest): Result<String>
    suspend fun updateIngotTransaction(transaction: UpdateIngotTransactionRequest): Result<Unit>
    fun fetchAllStoreSales(): Flow<Result<List<StoreTransaction>>>
    fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleTransaction>>>
}