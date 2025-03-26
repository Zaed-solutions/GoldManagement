package com.zaed.manager.ui.distributorssales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.authentication.request.FetchUsersByRoleRequest
import com.zaed.common.domain.authentication.FetchUsersByRoleUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.customer.FetchAllWholeCustomersUseCase
import com.zaed.common.domain.sale.FetchAllDistributorsSalesUseCase
import com.zaed.common.ui.util.toDate
import com.zaed.manager.ui.distributorssales.components.DistributorsSalesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DistributorsSalesViewModel(
    private val fetchSalesUseCase: FetchAllDistributorsSalesUseCase,
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchCashiersUseCase: FetchUsersByRoleUseCase,
    private val fetchCustomersUseCase: FetchAllWholeCustomersUseCase
) : ViewModel() {
    private val TAG: String = "DistributorsSalesViewModel"
    private val _uiState = MutableStateFlow(DistributorsSalesUiState())
    val uiState = _uiState.asStateFlow()

    fun init(startDate: String?, endDate: String?) {
        if (startDate != null && endDate !=null) {
            _uiState.update {
                it.copy(
                    filter = uiState.value.filter.copy(
                        startDate = startDate.toDate(),
                        endDate = endDate.toDate()
                    )
                )
            }
        }
        fetchSales()
        fetchCategories()
        fetchDistributors()
        fetchCustomers()
    }

    private fun fetchCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCustomersUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            customers = data
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCustomers: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchDistributors() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCashiersUseCase(
                FetchUsersByRoleRequest(
                    role = UserRole.DISTRIBUTOR
                )
            ).collect {
                it.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            employees = data
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchDistributors: ${e.message}", e)
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

    private fun fetchSales() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSalesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data,
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSales: ${e.message}", e)
                }
            }
        }
    }

    fun handleAction(action: DistributorsSalesUiAction) {
        when (action) {
            is DistributorsSalesUiAction.UpdateFilter -> updateFilter(action.filter)
            is DistributorsSalesUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
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

    private fun updateFilter(filter: DistributorsSalesFilter) {
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

                val employeeMatch =
                    !filter.isFiltered || filter.employees.isEmpty() ||
                            filter.employees.any { it.id == sale.distributorId }

                val customerMatch = !filter.isFiltered || filter.customers.isEmpty() ||
                        filter.customers.any { sale.accountId == it.id }

                val categoryMatch = !filter.isFiltered || filter.categories.isEmpty() ||
                        sale.products.any { product ->
                            filter.categories.any { category ->
                                product.categoryId == category.id
                            }
                        }
                val queryMatch = sale.receiptNumber.contains(uiState.value.searchQuery)
                dateInRange && employeeMatch && customerMatch && categoryMatch && queryMatch
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