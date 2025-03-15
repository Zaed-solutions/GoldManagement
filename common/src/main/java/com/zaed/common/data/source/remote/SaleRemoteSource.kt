package com.zaed.common.data.source.remote

import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.WholesaleGoldTransaction
import com.zaed.common.data.model.sale.WholesaleProductTransaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleProductSaleRequest
import kotlinx.coroutines.flow.Flow

interface SaleRemoteSource {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreTransaction>>>
    suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String>
    suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit>
    suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit>
    suspend fun getStoreSale(saleId: String): Result<StoreTransaction>
    fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit>
    suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleGoldSaleRequest): Result<Unit>
    suspend fun fetchWholesaleProductSale(request: FetchWholesaleProductSaleRequest): Result<WholesaleProductTransaction>
    suspend fun fetchWholesaleGoldSale(request: FetchWholesaleGoldSaleRequest): Result<WholesaleGoldTransaction>
    suspend fun addWholesaleProductSale(request: AddWholesaleProductSaleRequest): Result<String>
    suspend fun updateWholesaleProductSale(request: UpdateWholesaleProductSaleRequest): Result<Unit>
    fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleTransaction>>>
    suspend fun addGoldSale(request: AddWholesaleGoldSaleRequest): Result<String>
    suspend fun updateWholesaleGoldSale(request: UpdateWholesaleGoldSaleRequest): Result<Unit>
    fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>>
    suspend fun addIngotTransaction(transaction: AddIngotTransactionRequest): Result<String>
    suspend fun updateIngotTransaction(transaction: UpdateIngotTransactionRequest): Result<Unit>
    fun fetchAllStoreSales(): Flow<Result<List<StoreTransaction>>>
    fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleTransaction>>>
}