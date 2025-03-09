package com.zaed.manager.ui.storessales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.authentication.request.FetchUsersByRoleRequest
import com.zaed.common.domain.authentication.FetchUsersByRoleUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.sale.FetchAllStoreSalesUseCase
import com.zaed.common.domain.store.GetStoresUseCase
import com.zaed.manager.ui.storessales.components.StoreSalesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoresSalesViewModel(
    private val fetchSalesUseCase: FetchAllStoreSalesUseCase,
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchCashiersUseCase: FetchUsersByRoleUseCase,
    private val fetchStoresUseCase: GetStoresUseCase
) : ViewModel() {
    private val TAG: String = "StoresSalesViewModel"
    private val _uiState = MutableStateFlow(StoresSalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchStoresSales()
        fetchCategories()
        fetchCashiers()
        fetchStores()
    }

    private fun fetchStores() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoresUseCase().collect { result ->
                result.onSuccess { data ->
                    val locations = data.map { it.location }.toSortedSet()
                    _uiState.update { oldState ->
                        oldState.copy(
                            stores = data,
                            locations = locations
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchStores: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchCashiers() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCashiersUseCase(
                FetchUsersByRoleRequest(
                    role = UserRole.CASHIER
                )
            ).collect {
                it.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            employees = data
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCashiers: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllCategoriesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            categories = data
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCategories: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchStoresSales() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSalesUseCase().collect { result ->
                result.onSuccess { data ->
                    val customers = data.map { it.customerName }.toSortedSet() - ""
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data,
                            customers = customers
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchStoresSales: ${e.message}", e)
                }
            }
        }
    }

    fun handleAction(action: StoresSalesUiAction) {
        when (action) {
            is StoresSalesUiAction.UpdateFilter -> updateFilter(action.filter)
            is StoresSalesUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            else -> Unit
        }

    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = true,
                    searchQuery = query
                )
            }
            filterData()
        }
    }

    private fun updateFilter(filter: StoreSalesFilter) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    filter = filter,
                    isLoading = true
                )
            }
            filterData()
        }
    }

    private fun filterData() {
        viewModelScope.launch(Dispatchers.Default) {
            val filter = uiState.value.filter
            if (!filter.isFiltered && uiState.value.searchQuery.isBlank()) {
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        filteredSales = oldState.allSales
                    )
                }
                return@launch
            }

            val filteredSales = uiState.value.allSales.filter { sale ->
                val dateInRange = when {
                    !filter.isFiltered -> true
                    filter.startDate != null && filter.endDate != null ->
                        sale.createdAt in filter.startDate..filter.endDate

                    filter.startDate != null ->
                        sale.createdAt >= filter.startDate

                    filter.endDate != null ->
                        sale.createdAt <= filter.endDate

                    else -> true
                }

                val locationMatch =
                    !filter.isFiltered || filter.locations.isEmpty() ||
                        filter.locations.contains(sale.storeLocation)

                val employeeMatch =
                    !filter.isFiltered || filter.employees.isEmpty() ||
                        filter.employees.any { it.id == sale.employeeId }

                val customerMatch = !filter.isFiltered || filter.customers.isEmpty() ||
                        filter.customers.contains(sale.customerName)

                val categoryMatch = !filter.isFiltered ||filter.categories.isEmpty() ||
                        sale.products.any { product ->
                            filter.categories.any { category ->
                                product.categoryId == category.id
                            }
                        }
                val queryMatch = sale.receiptNumber.contains(uiState.value.searchQuery)
                dateInRange && locationMatch && employeeMatch && customerMatch && categoryMatch && queryMatch
            }
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = false,
                    filteredSales = filteredSales
                )
            }
        }
    }
}