package com.zaed.common.data.repository

import android.util.Log
import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import com.zaed.common.data.source.remote.SaleRemoteSource
import kotlinx.coroutines.flow.Flow

class SaleRepositoryImpl(
    private val saleRemoteSource: SaleRemoteSource
) : SaleRepository {
    override fun fetchStoreSales(request: FetchStoreSalesRequest) = saleRemoteSource.fetchStoreSales(request)
    override suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String> {
        return saleRemoteSource.addStoreSale(request)
    }

    override suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit> {
        return saleRemoteSource.deleteStoreSale(request)
    }

    override suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit> {
        return saleRemoteSource.updateStoreSale(request)
    }

    override suspend fun getStoreSale(saleId: String): Result<StoreTransaction> {
        return saleRemoteSource.getStoreSale(saleId)
    }

    override fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleTransaction>>> {
        return saleRemoteSource.fetchWholesaleDistributorSales(request)
    }

    override suspend fun deleteWholesale(request: DeleteWholesaleRequest): Result<Unit> {
        return saleRemoteSource.deleteWholesale(request)
    }



    override suspend fun fetchWholesale(request: FetchWholesaleRequest): Result<WholesaleTransaction> {
        return saleRemoteSource.fetchWholesale(request)
    }



    override suspend fun updateWholesale(request: UpdateWholesaleRequest): Result<Unit> {
        return saleRemoteSource.updateWholesale(request)
    }

    override suspend fun addWholesale(request: AddWholesaleRequest): Result<String> {
        Log.d("add_sale", "invoke repo: $request")
        return saleRemoteSource.addWholesale(request)
    }



    override fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>> {
        return saleRemoteSource.fetchIngotTransaction(request)
    }

    override suspend fun addIngotTransaction(transaction: AddIngotTransactionRequest): Result<String> {
        return saleRemoteSource.addIngotTransaction(transaction)
    }

    override suspend fun updateIngotTransaction(transaction: UpdateIngotTransactionRequest): Result<Unit> {
        return saleRemoteSource.updateIngotTransaction(transaction)
    }

    override fun fetchAllStoreSales(): Flow<Result<List<StoreTransaction>>> {
        return saleRemoteSource.fetchAllStoreSales()
    }

    override fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleTransaction>>> {
        return saleRemoteSource.fetchAllDistributorsSales()
    }

    override fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleTransaction>>> {
        return saleRemoteSource.fetchWholesaleCustomerSales(request)
    }
}