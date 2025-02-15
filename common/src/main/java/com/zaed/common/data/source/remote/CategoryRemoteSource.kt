package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.Product
import kotlinx.coroutines.flow.Flow

interface CategoryRemoteSource {
    fun fetchAllCategories(): Flow<Result<List<Category>>>
}