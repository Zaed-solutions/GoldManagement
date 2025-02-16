package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.data.source.remote.SaleRemoteSource

class SaleRepositoryImpl(
    private val saleRemoteSource: SaleRemoteSource
) : SaleRepository {
    override fun fetchStoreSales(request: FetchStoreSalesRequest) = saleRemoteSource.fetchStoreSales(request)
    override suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String> {
        return saleRemoteSource.addStoreSale(request)
    }

    override suspend fun getStoreSale(saleId: String): Result<StoreSale> {
        return saleRemoteSource.getStoreSale(saleId)
    }
}