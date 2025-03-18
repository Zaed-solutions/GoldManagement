package com.zaed.manager.ui.storedetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.domain.inventory.AddInventoryUseCase
import com.zaed.common.domain.inventory.FetchInventoriesUseCase
import com.zaed.common.domain.inventory.UpdateInventoryUseCase
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
import java.util.Date

class StoreDetailsViewModel(
    private val fetchLossesUseCase: FetchStoreLossesUseCase,
    private val convertLossesToDatedLossesUseCase: ConvertLossesToDatedLossesUseCase,
    private val fetchInventoriesUseCase: FetchInventoriesUseCase,
    private val fetchStoreSalesUseCase: FetchStoreSalesUseCase,
    private val fetchStoreUseCase: FetchStoreByIdUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val convertSalesToDatedSalesUseCase: ConvertSalesToDatedSalesUseCase,
    private val addInventoryUseCase: AddInventoryUseCase,
    private val updateInventoryUseCase: UpdateInventoryUseCase
) : ViewModel() {
    private val TAG: String = "StoreDetailsViewModel"
    private val _uiState = MutableStateFlow(StoreDetailsUiState())
    val uiState = _uiState.asStateFlow()
    fun init(storeId: String) {
        fetchStore(storeId)
        fetchSales(storeId)
        fetchStoreInventory(storeId)
        fetchLosses(storeId)
        fetchMainInventories()
    }

    private fun fetchMainInventories() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInventoriesUseCase(
                FetchInventoriesRequest("")
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            mainInventories = data
                        )
                    }
                }.onFailure {
                    Log.e(TAG, "fetchMainInventory: ${it.message}", it)
                }
            }
        }
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
                    if(uiState.value.selectedLossesFilter == DateFormat.CUSTOM_RANGE){
                        filterLosses()
                    } else {
                        convertToDatedLosses()
                    }
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

    private fun fetchStoreInventory(storeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInventoriesUseCase(
                FetchInventoriesRequest(storeId)
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allInventories = data.sortedBy { it.quantity }
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
                    Log.d(TAG, "fetchSales: $data")
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data.sortedByDescending { it.createdAt }
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
            is StoreDetailsUiAction.SetSalesDateRange -> setSalesDateRange(action.range)
            is StoreDetailsUiAction.OnInventoryQueryChanged -> updateInventoryQueryAndFilter(action.query)
            is StoreDetailsUiAction.OnSalesQueryChanged -> updateSalesQueryAndFilter(query = action.query)
            is StoreDetailsUiAction.OnUpdateStore -> updateStore(action.store)
            is StoreDetailsUiAction.UpdateLossesDateFilter -> updateLossesDateFilterAndFilter(action.format)
            is StoreDetailsUiAction.SetLossesDateRange -> setLossesDateRange(action.range)
            is StoreDetailsUiAction.OnSaveInventory -> saveInventory(action.inventory)
            else -> Unit
        }
    }

    private fun setLossesDateRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    selectedLossesFilter = DateFormat.CUSTOM_RANGE,
                    selectedLossesRange = range
                )
            }
            filterLosses()
        }
    }

    private fun filterLosses() {
        viewModelScope.launch (Dispatchers.Default){
            val filteredLosses = uiState.value.allLosses.filter{
                val beforeFlag = uiState.value.selectedLossesRange.first?.let { date ->
                    it.date >= date
                } ?: true
                val afterFlag = uiState.value.selectedLossesRange.second?.let { date ->
                    it.date  <= date
                } ?: true
                beforeFlag && afterFlag
            }
            _uiState.update { it.copy(isLoading = false, filteredLosses = filteredLosses) }
        }
    }

    private fun setSalesDateRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    selectedSalesFilter = DateFormat.CUSTOM_RANGE,
                    selectedSalesRange = range
                )
            }
            filterSales()
        }
    }

    private fun saveInventory(inventory: Inventory) {
        if(uiState.value.allInventories.any{it.productId == inventory.productId}){
            updateInventory(inventory)
        } else {
            addInventory(inventory)
        }
    }

    private fun updateInventory(inventory: Inventory) {
        viewModelScope.launch(Dispatchers.IO) {
            val mainInventory = _uiState.value.mainInventories.first { it.productId == inventory.productId}
            Log.d(TAG, "updateInventory: main: $mainInventory")
            val oldInventory = _uiState.value.allInventories.first { it.productId == inventory.productId}
            Log.d(TAG, "updateInventory: old: $oldInventory")
            val request = UpdateInventoryRequest(
                mainInventoryId = mainInventory.id,
                inventoryId = oldInventory.id,
                quantity = inventory.quantity
            )
            updateInventoryUseCase(request).onSuccess {
                Log.d(TAG, "updateInventory: success")
            }.onFailure {
                Log.e(TAG, "updateInventory: ${it.message}", it)
            }
        }
    }

    private fun addInventory(inventory: Inventory) {
        viewModelScope.launch (Dispatchers.IO){
            val newInventory = inventory.copy(
                ownerId = _uiState.value.store.id,
                ownerName = _uiState.value.store.name,
                lastUpdated = Date()
            )
            addInventoryUseCase(
                AddInventoryRequest(
                    mainInventoryId = inventory.id,
                    inventory = newInventory
                )
            ).onSuccess {
                Log.d(TAG, "addInventory: success")
            }.onFailure {
                Log.e(TAG, "addInventory: ${it.message}", it)
            }
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
                _uiState.update {oldState ->
                    oldState.copy(
                        filteredSales = oldState.allSales
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
            if(_uiState.value.selectedSalesFilter == DateFormat.CUSTOM_RANGE){
                val filteredSales = uiState.value.filteredSales.filter{
                    val beforeFlag = uiState.value.selectedSalesRange.first?.let { date ->
                        it.createdAt >= date
                    } ?: true
                    val afterFlag = uiState.value.selectedSalesRange.second?.let { date ->
                        it.createdAt <= date
                    } ?: true
                    beforeFlag && afterFlag
                }
                _uiState.update { it.copy(filteredSales = filteredSales) }
            } else {
                convertSalesToDatedSales()
            }
        }
    }

    private fun convertSalesToDatedSales() {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d(TAG, "convertSalesToDatedSales: ${_uiState.value.filteredSales}")
            convertSalesToDatedSalesUseCase(
                _uiState.value.filteredSales,
                _uiState.value.selectedSalesFilter
            ).let { datedSales ->
                _uiState.update {oldState ->
                    oldState.copy(
                        datedSales = datedSales
                    )
                }
                Log.d(TAG, "convertSalesToDatedSales: $datedSales")
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