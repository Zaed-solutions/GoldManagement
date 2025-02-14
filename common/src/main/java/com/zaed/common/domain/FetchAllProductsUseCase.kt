package com.zaed.common.domain

import com.zaed.common.data.repository.ProductRepository

class FetchAllProductsUseCase(
    private val productRepo: ProductRepository
) {
    operator fun invoke() = productRepo.fetchAllProducts()
}