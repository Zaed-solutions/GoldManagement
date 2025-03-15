package com.zaed.common.data.repository

import com.zaed.common.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun fetchAllCategories(): Flow<Result<List<Category>>>
    suspend fun addCategory(category: Category): Result<Unit>
}