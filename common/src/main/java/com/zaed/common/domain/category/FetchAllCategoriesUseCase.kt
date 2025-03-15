package com.zaed.common.domain.category

import com.zaed.common.data.repository.CategoryRepository

class FetchAllCategoriesUseCase(
    private val productRepo: CategoryRepository
) {
    operator fun invoke() = productRepo.fetchAllCategories()
}
