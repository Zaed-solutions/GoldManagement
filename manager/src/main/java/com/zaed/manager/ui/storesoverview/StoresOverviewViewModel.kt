package com.zaed.manager.ui.storesoverview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.authentication.request.FetchUsersByRoleRequest
import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.domain.authentication.FetchUsersByRoleUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.sale.FetchAllStoreSalesUseCase
import com.zaed.common.domain.store.AddStoreUseCase
import com.zaed.common.domain.store.DeleteStoreUseCase
import com.zaed.common.domain.store.GetStoresUseCase
import com.zaed.common.domain.store.UpdateStoreUseCase
import com.zaed.common.ui.util.isAfter
import com.zaed.common.ui.util.isBefore
import com.zaed.manager.ui.storessales.components.StoreSalesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoresOverviewViewModel(
    private val getStoresUseCase: GetStoresUseCase,
    private val addStoreUseCase: AddStoreUseCase,
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val fetchSalesUseCase: FetchAllStoreSalesUseCase,
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchCashiersUseCase: FetchUsersByRoleUseCase,
): ViewModel() {
    private val TAG = "StoresOverviewViewModel"
    private val _uiState = MutableStateFlow(StoresOverviewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchStores()
        fetchStoresSales()
        fetchCategories()
        fetchCashiers()
    }

    private fun fetchStores(){
        viewModelScope.launch (Dispatchers.IO){
            getStoresUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            isStoresLoading = false,
                            stores = data
                        )
                    }
                }.onFailure {
                    Log.e(TAG, "fetchStores: ",)
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
                    filterSales()
                }.onFailure { e ->
                    Log.e(TAG, "fetchStoresSales: ${e.message}", e)
                }
            }
        }
    }

    private fun filterSales() {
        viewModelScope.launch(Dispatchers.Default) {
            val filter = uiState.value.salesFilter
            if (!filter.isFiltered && uiState.value.salesSearchQuery.isBlank()) {
                _uiState.update { oldState ->
                    oldState.copy(
                        isStoresSalesLoading = false,
                        filteredSales = oldState.allSales
                    )
                }
                return@launch
            }

            val filteredSales = uiState.value.allSales.filter { sale ->
                val afterFlag =
                    !filter.isFiltered||filter.startDate?.let {
                        sale.createdAt.isAfter(it)
                    }
                            ?: true
                val beforeFlag = !filter.isFiltered||filter.endDate?.let {
                    sale.createdAt.isBefore(it)
                } ?: true

                val locationMatch =
                    !filter.isFiltered || filter.locations.isEmpty() ||
                            filter.locations.contains(sale.storeLocation)

                val employeeMatch =
                    !filter.isFiltered || filter.employees.isEmpty() ||
                            filter.employees.any { it.id == sale.employeeId }

                val customerMatch = !filter.isFiltered || filter.customers.isEmpty() ||
                        filter.customers.contains(sale.customerName)

                val categoryMatch = !filter.isFiltered || filter.categories.isEmpty() ||
                        sale.products.any { product ->
                            filter.categories.any { category ->
                                product.categoryId == category.id
                            }
                        }
                val queryMatch = sale.receiptNumber.contains(uiState.value.salesSearchQuery)
                locationMatch && employeeMatch && customerMatch && categoryMatch && queryMatch && beforeFlag && afterFlag

            }
            _uiState.update { oldState ->
                oldState.copy(
                    isStoresSalesLoading = false,
                    filteredSales = filteredSales
                )
            }
        }
    }

    fun handleAction(action: StoresOverviewUiAction){
        when(action){
            is StoresOverviewUiAction.OnAddStore -> addStore(action.store)
            is StoresOverviewUiAction.OnDeleteStore -> deleteStore(action.store)
            is StoresOverviewUiAction.OnUpdateStore -> updateStore(action.store)
            is StoresOverviewUiAction.UpdateSalesFilter -> updateFilter(action.filter)
            is StoresOverviewUiAction.UpdateSalesSearchQuery -> updateSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun addStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            addStoreUseCase(
                AddStoreRequest(
                    store = store
                )
            ).onSuccess {
                Log.d(TAG, "addStore: success")
            }.onFailure {
                Log.e(TAG, "addStore: ", )
            }
        }
    }

    private fun updateStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            updateStoreUseCase(
                UpdateStoreRequest(
                    store = store
                )
            ).onSuccess {
                Log.d(TAG, "updateStore: success")
            }.onFailure {
                Log.e(TAG, "updateStore: ", )
            }
        }
    }

    private fun deleteStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            deleteStoreUseCase(
                DeleteStoreRequest(
                    store = store
                )
            ).onSuccess {
                Log.d(TAG, "deleteStore: success")
            }.onFailure {
                Log.e(TAG, "deleteStore: ", )
            }
        }
    }


    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isStoresSalesLoading = true,
                     salesSearchQuery = query
                )
            }
            filterSales()
        }
    }

    private fun updateFilter(filter: StoreSalesFilter) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    salesFilter = filter,
                    isStoresSalesLoading = true
                )
            }
            filterSales()
        }
    }
}