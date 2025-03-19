package com.zaed.manager.ui.distributordetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.authentication.request.FetchDistributorRequest
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.InventoryType
import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.domain.authentication.FetchDistributorUseCase
import com.zaed.common.domain.inventory.AddInventoryUseCase
import com.zaed.common.domain.inventory.FetchInventoriesUseCase
import com.zaed.common.domain.inventory.UpdateInventoryUseCase
import com.zaed.common.domain.loss.ConvertLossesToDatedLossesUseCase
import com.zaed.common.domain.loss.FetchDistributorLossesUseCase
import com.zaed.common.domain.sale.ConvertIngotTransactionsToDatedUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.domain.sale.FetchDistributorSalesUseCase
import com.zaed.common.domain.sale.FetchIngotTransactionsUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.isAfter
import com.zaed.common.ui.util.isBefore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class DistributorDetailsViewModel(
    private val fetchDistributorUseCase: FetchDistributorUseCase,
    private val convertSalesToDatedSalesUseCase: ConvertSalesToDatedSalesUseCase,
    private val addInventoryUseCase: AddInventoryUseCase,
    private val updateInventoryUseCase: UpdateInventoryUseCase,
    private val convertLossesToDatedLossesUseCase: ConvertLossesToDatedLossesUseCase,
    private val fetchIngotTransactionsUseCase: FetchIngotTransactionsUseCase,
    private val fetchInventoriesUseCase: FetchInventoriesUseCase,
    private val convertIngotTransactionsUseCase: ConvertIngotTransactionsToDatedUseCase,
    private val fetchDistributorLossesUseCase: FetchDistributorLossesUseCase,
    private val fetchDistributorSalesUseCase: FetchDistributorSalesUseCase
) : ViewModel() {
    private val TAG: String = DistributorDetailsViewModel::class.java.simpleName
    private val _uiState = MutableStateFlow(DistributorDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(distributorId: String) {
        fetchDistributor(distributorId)
    }

    private fun fetchDistributor(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDistributorUseCase(
                FetchDistributorRequest(
                    distributorId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(distributor = data)
                }
                fetchSales(distributorId)
                fetchInventory(distributorId, data.permissions)
                if (data.permissions.contains(UserPermission.SELL_INGOTS)) {
                    fetchIngotTransactions(distributorId)
                }
                fetchLosses(distributorId)
                fetchMainInventories(data.permissions)
            }.onFailure {
                Log.e(TAG, "fetchDistributor: ${it.message}", it)
            }
        }
    }

    private fun fetchIngotTransactions(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchIngotTransactionsUseCase(
                FetchIngotTransactionsRequest(
                    distributorId = distributorId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allIngotTransactions = data
                        )
                    }
                    if (uiState.value.ingotTransactionsDateFormat == DateFormat.CUSTOM_RANGE) {
                        filterIngots()
                    } else {
                        convertIngotTransactionsToDated()
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchIngotTransactions: ${e.message}", e)
                }
            }
        }
    }

    private fun convertIngotTransactionsToDated() {
        viewModelScope.launch(Dispatchers.Default) {
            convertIngotTransactionsUseCase(
                uiState.value.allIngotTransactions,
                uiState.value.ingotTransactionsDateFormat
            ).let { datedItems ->
                _uiState.update { oldState ->
                    oldState.copy(
                        datedIngotTransactions = datedItems
                    )
                }
            }
        }
    }

    private fun fetchMainInventories(permissions: List<UserPermission>) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInventoriesUseCase(
                FetchInventoriesRequest(
                    ownerId = "",
                    permissions = permissions
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            mainInventories = data
                        )
                    }
                    Log.d(TAG, "fetchMainInventories: $data")
                }.onFailure { e ->
                    Log.e(TAG, "fetchMainInventories: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchLosses(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDistributorLossesUseCase(
                FetchDistributorLossesRequest(
                    distributorId = distributorId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allLosses = data
                        )
                    }
                    if (uiState.value.selectedLossesFilter == DateFormat.CUSTOM_RANGE) {
                        filterLosses()
                    } else {
                        convertToDatedLosses()
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchLosses: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchInventory(distributorId: String, permissions: List<UserPermission>) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInventoriesUseCase(
                FetchInventoriesRequest(
                    ownerId = distributorId,
                    permissions = permissions
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allInventories = data
                        )
                    }
                    filterInventory()
                }.onFailure { e ->
                    Log.e(TAG, "fetchInventory: ${e.message}", e)
                }
            }
        }
    }

    private fun fetchSales(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDistributorSalesUseCase(
                FetchDistributorSalesRequest(
                    distributorId = distributorId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data
                        )
                    }
                    filterSales()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSales: ${e.message}", e)
                }
            }
        }
    }

    fun handleAction(action: DistributorDetailsUiAction) {
        when (action) {
            is DistributorDetailsUiAction.OnInventoryQueryChanged -> updateInventoryQueryAndFilter(
                action.query
            )

            is DistributorDetailsUiAction.OnSalesQueryChanged -> updateSalesQueryAndFilter(action.query)
            is DistributorDetailsUiAction.OnSaveInventory -> saveInventory(action.inventory)
            is DistributorDetailsUiAction.UpdateLossesDateFilter -> updateLossesDateFilterAndFilter(
                action.dateFormat
            )

            is DistributorDetailsUiAction.SetSalesDateRange -> setCustomRangeFilter(action.range)
            is DistributorDetailsUiAction.UpdateSalesDateFilter -> updateSalesDateFilterAndFilter(
                action.dateFormat
            )

            is DistributorDetailsUiAction.UpdateIngotTransactionsDateFilter -> updateIngotTransactionsDateFilterAndFilter(
                action.dateFormat
            )

            is DistributorDetailsUiAction.SetLossesDateRange -> setLossesDateRange(action.range)
            is DistributorDetailsUiAction.SetIngotTransactionsDateRange -> setIngotTransactionsDateRange(
                action.range
            )

            else -> Unit
        }
    }

    private fun setIngotTransactionsDateRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if (range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    ingotTransactionsDateFormat = DateFormat.CUSTOM_RANGE,
                    selectedIngotsRange = range
                )
            }
            filterIngots()
        }
    }

    private fun filterIngots() {
        viewModelScope.launch(Dispatchers.Default) {
            val filteredTransactions = uiState.value.allIngotTransactions.filter { transaction ->
                val afterFlag =
                    uiState.value.selectedIngotsRange.first?.let { transaction.createdAt.isAfter(it)}
                        ?: true
                val beforeFlag =
                    uiState.value.selectedIngotsRange.second?.let {
                        transaction.createdAt.isBefore(it)
                    } ?: true
                beforeFlag && afterFlag
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    filteredIngotTransactions = filteredTransactions
                )
            }
        }
    }

    private fun setLossesDateRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if (range.first == null && range.second == null) return@launch
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
        viewModelScope.launch(Dispatchers.Default) {
            val filteredLosses = uiState.value.allLosses.filter { loss ->
                val afterFlag =
                    uiState.value.selectedLossesRange.first?.let { loss.date.isAfter(it) } ?: true
                val beforeFlag =
                    uiState.value.selectedLossesRange.second?.let {
                        loss.date.isBefore(it)
                    } ?: true
                beforeFlag && afterFlag
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    filteredLosses = filteredLosses
                )
            }
        }
    }

    private fun setCustomRangeFilter(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if (range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    selectedSalesFilter = DateFormat.CUSTOM_RANGE,
                    selectedSalesDateRange = range
                )
            }
            filterSales()
        }
    }

    private fun updateIngotTransactionsDateFilterAndFilter(dateFormat: DateFormat) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ingotTransactionsDateFormat = dateFormat
                )
            }
            convertIngotTransactionsToDated()
        }
    }

    private fun saveInventory(inventory: Inventory) {
        if (isOldInventory(inventory)) {
            updateInventory(inventory)
        } else {
            addInventory(inventory)
        }
    }

    private fun isOldInventory(inventory: Inventory): Boolean {
        return isOldProductInventory(inventory) || isOldGoldInventory(inventory) || isOldIngotInventory(
            inventory
        )
    }

    private fun isOldIngotInventory(inventory: Inventory): Boolean {
        return inventory.type == InventoryType.INGOT && uiState.value.allInventories.any { it.type == InventoryType.INGOT && it.karat == inventory.karat }
    }

    private fun isOldProductInventory(inventory: Inventory): Boolean {
        return uiState.value.allInventories.any { it.productId == inventory.productId }
    }

    private fun isOldGoldInventory(inventory: Inventory): Boolean {
        return inventory.type == InventoryType.GOLD && uiState.value.allInventories.any { it.type == InventoryType.GOLD }
    }

    private fun updateInventory(inventory: Inventory) {
        viewModelScope.launch(Dispatchers.IO) {
            val mainInventory =
                _uiState.value.mainInventories.first { it.productId == inventory.productId }
            Log.d(TAG, "updateInventory: main: $mainInventory")
            val oldInventory =
                _uiState.value.allInventories.first { it.productId == inventory.productId }
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
        viewModelScope.launch(Dispatchers.IO) {
            val newInventory = inventory.copy(
                ownerId = _uiState.value.distributor.id,
                ownerName = _uiState.value.distributor.fullName,
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
                _uiState.update { oldState ->
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
            if (uiState.value.selectedSalesFilter == DateFormat.CUSTOM_RANGE) {
                val filteredSales = uiState.value.filteredSales.filter { sale ->
                    val afterFlag =
                        uiState.value.selectedSalesDateRange.first?.let {
                            sale.createdAt.isAfter(it)
                        }
                            ?: true
                    val beforeFlag = uiState.value.selectedSalesDateRange.second?.let {
                        sale.createdAt.isBefore(it)
                    } ?: true
                    beforeFlag && afterFlag
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        filteredSales = filteredSales
                    )
                }
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
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        datedSales = datedSales
                    )
                }
                Log.d(TAG, "convertSalesToDatedSales: $datedSales")
            }
        }
    }
}