package com.zaed.common.data.repository

import com.zaed.common.data.model.Category
import com.zaed.common.data.source.remote.CategoryRemoteSource
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource
) : CategoryRepository {
    override fun fetchAllCategories(): Flow<Result<List<Category>>> {
        return categoryRemoteSource.fetchAllCategories()
    }
}