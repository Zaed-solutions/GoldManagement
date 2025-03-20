package com.zaed.manager.ui.categories

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.CategoryWithInventory
import com.zaed.common.data.model.inventory.Inventory

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val allCategoriesWithInventories: List<CategoryWithInventory> = emptyList(),
    val filteredCategoriesWithInventories: List<CategoryWithInventory> = emptyList()
)
