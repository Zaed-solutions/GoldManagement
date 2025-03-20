package com.zaed.common.data.repository

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.request.AddCategoryRequest
import com.zaed.common.data.model.category.request.DeleteCategoryRequest
import com.zaed.common.data.model.category.request.UpdateCategoryRequest
import com.zaed.common.data.source.remote.CategoryRemoteSource
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource
) : CategoryRepository {
    override fun fetchAllCategories(): Flow<Result<List<Category>>> {
        return categoryRemoteSource.fetchAllCategories()
    }

    override suspend fun addCategory(request: AddCategoryRequest): Result<Unit> {
        return categoryRemoteSource.addCategory(request)
    }

    override suspend fun updateCategory(request: UpdateCategoryRequest): Result<Unit> {
        return categoryRemoteSource.updateCategory(request)
    }

    override suspend fun deleteCategory(request: DeleteCategoryRequest): Result<Unit> {
        return categoryRemoteSource.deleteCategory(request)
    }
}