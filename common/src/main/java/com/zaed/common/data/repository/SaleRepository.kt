package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>>
    suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String>
}