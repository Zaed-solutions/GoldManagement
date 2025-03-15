package com.zaed.common.data.repository

import android.util.Log
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

    override suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit> {
        return saleRemoteSource.deleteWholesaleProductSale(request)
    }

    override suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleGoldSaleRequest): Result<Unit> {
        return saleRemoteSource.deleteWholesaleGoldSale(request)
    }

    override suspend fun fetchWholesaleProductSale(request: FetchWholesaleProductSaleRequest): Result<WholesaleProductTransaction> {
        return saleRemoteSource.fetchWholesaleProductSale(request)
    }

    override suspend fun fetchWholesaleGoldSale(request: FetchWholesaleGoldSaleRequest): Result<WholesaleGoldTransaction> {
        return saleRemoteSource.fetchWholesaleGoldSale(request)
    }

    override suspend fun updateWholesaleGoldSale(request: UpdateWholesaleGoldSaleRequest): Result<Unit> {
        return saleRemoteSource.updateWholesaleGoldSale(request)
    }

    override suspend fun addWholesaleProductSale(request: AddWholesaleProductSaleRequest): Result<String> {
        Log.d("add_sale", "invoke repo: $request")
        return saleRemoteSource.addWholesaleProductSale(request)
    }

    override suspend fun addGoldSale(request: AddWholesaleGoldSaleRequest): Result<String> {
        return saleRemoteSource.addGoldSale(request)
    }

    override suspend fun updateWholesaleProductSale(request: UpdateWholesaleProductSaleRequest): Result<Unit> {
        return saleRemoteSource.updateWholesaleProductSale(request)
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