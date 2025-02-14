package com.zaed.common.data.repository

import com.zaed.common.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun fetchAllProducts(): Flow<Result<List<Product>>>
}