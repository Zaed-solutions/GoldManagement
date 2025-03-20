package com.zaed.common.domain.category

import com.zaed.common.data.model.category.request.UpdateCategoryRequest
import com.zaed.common.data.repository.CategoryRepository

class UpdateCategoryUseCase(
    private val categoryRepo: CategoryRepository
) {
    suspend operator fun invoke(request: UpdateCategoryRequest) = categoryRepo.updateCategory(request)
}