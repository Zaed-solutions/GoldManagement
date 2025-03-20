package com.zaed.common.data.model.category.request

import com.zaed.common.data.model.category.Category

data class UpdateCategoryRequest(
    val category: Category = Category(),
    val availableAmount: Double = 0.0,
)
