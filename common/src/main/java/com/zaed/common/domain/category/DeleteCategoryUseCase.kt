package com.zaed.common.domain.category

import com.zaed.common.data.model.category.request.DeleteCategoryRequest
import com.zaed.common.data.repository.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepo: CategoryRepository
) {
    suspend operator fun invoke(request: DeleteCategoryRequest) = categoryRepo.deleteCategory(request)
}