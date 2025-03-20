package com.zaed.manager.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.category.CategoryWithInventory
import com.zaed.common.data.model.category.request.AddCategoryRequest
import com.zaed.common.data.model.category.request.DeleteCategoryRequest
import com.zaed.common.data.model.category.request.UpdateCategoryRequest
import com.zaed.common.domain.category.AddCategoryUseCase
import com.zaed.common.domain.category.DeleteCategoryUseCase
import com.zaed.common.domain.category.FetchCategoriesWithInventoriesUseCase
import com.zaed.common.domain.category.UpdateCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel (
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val fetchCategoriesWithInventoriesUseCase: FetchCategoriesWithInventoriesUseCase
): ViewModel() {
    private val TAG: String = "CategoriesViewModel"
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCategoriesWithInventories()
    }

    private fun fetchCategoriesWithInventories() {
        viewModelScope.launch (Dispatchers.IO){
            fetchCategoriesWithInventoriesUseCase().collect{ result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            allCategoriesWithInventories = data
                        )
                    }
                    filterData()
                }.onFailure {
                    Log.e(TAG, "fetchCategoriesWithInventories: ${it.message}",it )
                }
            }
        }
    }

    fun handleAction(action: CategoriesUiAction) {
        when (action) {
            is CategoriesUiAction.CreateCategory -> createCategory(action.category)
            is CategoriesUiAction.DeleteCategory -> deleteCategory(action.categoryWithInventory)
            is CategoriesUiAction.UpdateCategory -> updateCategory(
                action.newCategory
            )

            is CategoriesUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, searchQuery = query) }
            filterData()
        }
    }

    private fun filterData() {
        viewModelScope.launch(Dispatchers.Default) {
            val query = uiState.value.searchQuery
            val filteredCategories = if(query.isBlank()){
                uiState.value.allCategoriesWithInventories
            } else {
                uiState.value.allCategoriesWithInventories.filter {
                    it.category.name.contains(query)
                }
            }
            _uiState.update {
                it.copy(
                    filteredCategoriesWithInventories = filteredCategories,
                    isLoading = false
                )
            }
        }
    }

    private fun updateCategory(
        newCategory: CategoryWithInventory
    ) {
        viewModelScope.launch (Dispatchers.IO){
            updateCategoryUseCase(
                UpdateCategoryRequest(
                    category = newCategory.category,
                    availableAmount = newCategory.inventory.quantity
                )
            ).onSuccess {
                Log.d(TAG, "updateCategory: success")
            }.onFailure {
                Log.e(TAG, "updateCategory: ${it.message}", it)
            }
        }
    }

    private fun deleteCategory(categoryWithInventory: CategoryWithInventory) {
        viewModelScope.launch (Dispatchers.IO){
            deleteCategoryUseCase(
                DeleteCategoryRequest(
                    categoryWithInventory.category.id
                )
            ).onSuccess {
                Log.d(TAG, "deleteCategory: success")
            }.onFailure {
                Log.e(TAG, "deleteCategory: ${it.message}", it)
            }
        }
    }

    private fun createCategory(category: CategoryWithInventory) {
        viewModelScope.launch (Dispatchers.IO){
            addCategoryUseCase(
                AddCategoryRequest(
                    category = category.category,
                    availableAmount = category.inventory.quantity
                )
            ).onSuccess {
                Log.d(TAG, "createCategory: success")
            }.onFailure {
                Log.e(TAG, "createCategory: ${it.message}", it)
            }
        }
    }
}