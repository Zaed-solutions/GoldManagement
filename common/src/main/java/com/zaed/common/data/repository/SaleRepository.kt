package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>>
}