package com.zaed.common.domain.category

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.request.AddCategoryRequest
import com.zaed.common.data.repository.CategoryRepository

class AddCategoryUseCase(
    private val productRepo: CategoryRepository
) {
    suspend operator fun invoke(request: AddCategoryRequest) = productRepo.addCategory(request)
}