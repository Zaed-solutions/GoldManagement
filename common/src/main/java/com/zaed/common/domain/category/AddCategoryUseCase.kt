package com.zaed.common.domain.category

import com.zaed.common.data.model.Category
import com.zaed.common.data.repository.CategoryRepository

class AddCategoryUseCase(
    private val productRepo: CategoryRepository
) {
    suspend operator fun invoke(category: Category) = productRepo.addCategory(category)
}