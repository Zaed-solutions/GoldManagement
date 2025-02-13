package com.zaed.common.data.source.remote

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import kotlinx.coroutines.flow.Flow

interface SaleRemoteSource {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>>
}