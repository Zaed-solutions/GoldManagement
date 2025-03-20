package com.zaed.common.data.repository

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.request.AddCategoryRequest
import com.zaed.common.data.model.category.request.DeleteCategoryRequest
import com.zaed.common.data.model.category.request.UpdateCategoryRequest
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun fetchAllCategories(): Flow<Result<List<Category>>>
    suspend fun addCategory(request: AddCategoryRequest): Result<Unit>
    suspend fun updateCategory(request: UpdateCategoryRequest): Result<Unit>
    suspend fun deleteCategory(request: DeleteCategoryRequest): Result<Unit>
}