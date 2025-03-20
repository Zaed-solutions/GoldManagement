package com.zaed.manager.ui.categories

import com.zaed.common.data.model.category.CategoryWithInventory

sealed interface CategoriesUiAction {
    data object ShowNavDrawer : CategoriesUiAction
    data class UpdateSearchQuery(val query: String) : CategoriesUiAction
    data class DeleteCategory(val categoryWithInventory: CategoryWithInventory) : CategoriesUiAction
    data class UpdateCategory(
        val newCategory: CategoryWithInventory
    ) : CategoriesUiAction
    data class CreateCategory(
        val category: CategoryWithInventory
    ): CategoriesUiAction
}