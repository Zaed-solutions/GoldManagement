package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.WholesaleSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.data.model.request.UpdateStoreSaleRequest
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

    override suspend fun getStoreSale(saleId: String): Result<StoreSale> {
        return saleRemoteSource.getStoreSale(saleId)
    }

    override fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleSale>>> {
        return saleRemoteSource.fetchWholesaleDistributorSales(request)
    }

    override suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit> {
        return saleRemoteSource.deleteWholesaleProductSale(request)
    }

    override suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleGoldSaleRequest): Result<Unit> {
        return saleRemoteSource.deleteWholesaleGoldSale(request)
    }
}