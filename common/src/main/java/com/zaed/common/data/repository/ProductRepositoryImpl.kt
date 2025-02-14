package com.zaed.common.data.repository

import com.zaed.common.data.model.Product
import com.zaed.common.data.source.remote.ProductRemoteSource
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productRemoteSource: ProductRemoteSource
) : ProductRepository {
    override fun fetchAllProducts(): Flow<Result<List<Product>>> {
        return productRemoteSource.fetchAllProducts()
    }
}