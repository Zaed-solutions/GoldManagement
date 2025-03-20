package com.zaed.common.data.model.category.request

import com.zaed.common.data.model.category.Category

data class AddCategoryRequest(
    val category: Category = Category(),
    val availableAmount: Double = 0.0,
)
