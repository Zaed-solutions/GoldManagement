package com.zaed.manager.ui.storedetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.domain.inventory.FetchStoreInventoryUseCase
import com.zaed.common.domain.loss.ConvertLossesToDatedLossesUseCase
import com.zaed.common.domain.loss.FetchStoreLossesUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.domain.sale.FetchStoreSalesUseCase
import com.zaed.common.domain.store.DeleteStoreUseCase
import com.zaed.common.domain.store.FetchStoreByIdUseCase
import com.zaed.common.domain.store.UpdateStoreUseCase
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreDetailsViewModel(
    private val fetchLossesUseCase: FetchStoreLossesUseCase,
    private val convertLossesToDatedLossesUseCase: ConvertLossesToDatedLossesUseCase,
    private val fetchStoreInventoryUseCase: FetchStoreInventoryUseCase,
    private val fetchStoreSalesUseCase: FetchStoreSalesUseCase,
    private val fetchStoreUseCase: FetchStoreByIdUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val convertSalesToDatedSalesUseCase: ConvertSalesToDatedSalesUseCase
) : ViewModel() {
    private val TAG: String = "StoreDetailsViewModel"
    private val _uiState = MutableStateFlow(StoreDetailsUiState())
    val uiState = _uiState.asStateFlow()
    fun init(storeId: String) {
        fetchStore(storeId)
        fetchSales(storeId)
        fetchInventory(storeId)
        fetchLosses(storeId)
    }

    private fun fetchLosses(storeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchLossesUseCase(
                FetchStoreLossesRequest(storeId)
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allLosses = data
                        )
                    }
                    convertToDatedLosses()
                }.onFailure {
                    Log.e(TAG, "fetchLosses: ${it.message}", it)
                }
            }
        }
    }

    private fun convertToDatedLosses() {
        viewModelScope.launch(Dispatchers.Default) {
            val datedLosses = convertLossesToDatedLossesUseCase(
                _uiState.value.allLosses,
                _uiState.value.selectedLossesFilter
            )
            _uiState.update {
                it.copy(
                    datedLosses = datedLosses
                )
            }
        }
    }

    private fun fetchInventory(storeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoreInventoryUseCase(
                FetchStoreInventoryRequest(storeId)
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allInventories = data
                        )
                    }
                    filterInventory()
                }.onFailure {
                    Log.e(TAG, "fetchInventory: ${it.message}", it)
                }
            }
        }
    }

    private fun fetchSales(storeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoreSalesUseCase(
                FetchStoreSalesRequest(
                    storeId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data
                        )
                    }
                    filterSales()
                }.onFailure {
                    Log.e(TAG, "fetchSales: ${it.message}", it)
                }
            }
        }
    }

    private fun fetchStore(storeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoreUseCase(
                FetchStoreByIdRequest(storeId)
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        store = data
                    )
                }
            }.onFailure {
                Log.e(TAG, "fetchStore: ${it.message}", it)
            }
        }
    }

    fun handleAction(action: StoreDetailsUiAction) {
        when (action) {
            StoreDetailsUiAction.OnDeleteStore -> deleteStore()
            is StoreDetailsUiAction.UpdateSalesDateFilter -> updateSalesDateFilterAndFilter(format = action.format)
            is StoreDetailsUiAction.OnInventoryQueryChanged -> updateInventoryQueryAndFilter(action.query)
            is StoreDetailsUiAction.OnSalesQueryChanged -> updateSalesQueryAndFilter(query = action.query)
            is StoreDetailsUiAction.OnUpdateStore -> updateStore(action.store)
            is StoreDetailsUiAction.UpdateLossesDateFilter -> updateLossesDateFilterAndFilter(action.format)
            else -> Unit
        }
    }

    private fun updateLossesDateFilterAndFilter(format: DateFormat) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedLossesFilter = format
                )
            }
            convertToDatedLosses()
        }
    }

    private fun updateSalesQueryAndFilter(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    salesQuery = query
                )
            }
            filterSales()
        }
    }

    private fun updateSalesDateFilterAndFilter(format: DateFormat) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedSalesFilter = format
                )
            }
            filterSales()
        }
    }

    private fun updateInventoryQueryAndFilter(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    inventoryQuery = query
                )
            }
            filterInventory()
        }
    }

    private fun filterInventory() {
        viewModelScope.launch(Dispatchers.Default) {
            if (_uiState.value.inventoryQuery.isBlank()) {
                _uiState.update {
                    it.copy(
                        displayedInventories = it.allInventories
                    )
                }
            } else {
                val filteredInventories = _uiState.value.allInventories.filter {
                    it.productName.contains(
                        _uiState.value.inventoryQuery,
                        ignoreCase = true
                    )
                }
                _uiState.update {
                    it.copy(
                        displayedInventories = filteredInventories
                    )
                }
            }
        }
    }

    private fun filterSales() {
        viewModelScope.launch(Dispatchers.Default) {
            if (_uiState.value.salesQuery.isBlank()) {
                _uiState.update {
                    it.copy(
                        filteredSales = it.allSales
                    )
                }
            } else {

                val filteredSales = _uiState.value.allSales.filter { sale ->
                    listOf(sale.receiptNumber).any {
                        it.contains(
                            _uiState.value.salesQuery,
                            ignoreCase = true
                        )
                    }
                }
                _uiState.update {
                    it.copy(
                        filteredSales = filteredSales
                    )
                }
            }
            convertSalesToDatedSales()
        }
    }

    private fun convertSalesToDatedSales() {
        viewModelScope.launch(Dispatchers.Default) {
            convertSalesToDatedSalesUseCase(
                _uiState.value.filteredSales,
                _uiState.value.selectedSalesFilter
            ).let { datedSales ->
                _uiState.update {
                    it.copy(
                        datedSales = datedSales
                    )
                }
            }
        }
    }

    private fun deleteStore() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteStoreUseCase(
                DeleteStoreRequest(
                    store = _uiState.value.store
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isDeleted = true
                    )
                }
            }.onFailure {
                Log.e(TAG, "deleteStore: ${it.message}", it)
            }
        }
    }

    private fun updateStore(store: Store) {
        viewModelScope.launch(Dispatchers.IO) {
            updateStoreUseCase(
                UpdateStoreRequest(
                    store = store
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        store = it.store.copy(
                            name = store.name,
                            location = store.location
                        )
                    )
                }
            }.onFailure {
                Log.e(TAG, "updateStore: ${it.message}", it)
            }
        }
    }
}