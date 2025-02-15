package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.DeleteStoreSaleRequest
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
}