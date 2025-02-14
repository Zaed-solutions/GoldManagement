package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRemoteSource {
    fun fetchAllProducts(): Flow<Result<List<Product>>>
}