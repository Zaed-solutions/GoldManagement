package com.zaed.cashier.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.domain.sale.DeleteStoreSaleUseCase
import com.zaed.common.domain.sale.FetchStoreSalesUseCase
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class SalesViewModel(
    private val fetchStoreSalesUseCase: FetchStoreSalesUseCase,
    private val deleteStoreSaleUseCase: DeleteStoreSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val convertSalesToDatedSalesUseCase: ConvertSalesToDatedSalesUseCase,
) : ViewModel() {
    private val TAG: String = "SalesViewModel"
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                    fetchSales()
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchSales() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoreSalesUseCase(
                FetchStoreSalesRequest(
                    storeId = uiState.value.currentUser.storeId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allSales = data
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSales: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleAction(action: SalesUiAction) {
        when (action) {
            is SalesUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            is SalesUiAction.OnDeleteSale -> deleteSale(action.saleId)
            is SalesUiAction.UpdateSelectedDate -> updateDateFilter(action.filter)
            is SalesUiAction.SetCustomRangeFilter -> setCustomRangeFilter(action.range)
            else -> Unit
        }
    }

    private fun updateDateFilter(filter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedDateFilter = filter) }
            convertToDatedSales()
        }
    }

    private fun deleteSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteStoreSaleUseCase(
                DeleteStoreSaleRequest(
                    saleId = saleId,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteSale: success")
            }.onFailure { e ->
                Log.e(TAG, "deleteSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
            filterData(query)
        }
    }

    private fun setCustomRangeFilter(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update { oldState -> oldState.copy(isLoading = true, selectedDateFilter = DateFormat.CUSTOM_RANGE, selectedDateRange = range) }
            filterData()
        }
    }

    private fun filterData(
        query: String = uiState.value.searchQuery,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            with(uiState.value.allSales) {
                if (query.isBlank()) {
                    _uiState.update { it.copy(filteredSales = this) }
                } else {
                    val filteredSales =
                        filter {
                            listOf(it.customerName, it.receiptNumber).any {
                                it.contains(
                                    query,
                                    ignoreCase = true
                                )
                            }
                        }
                    _uiState.update { it.copy(filteredSales = filteredSales) }
                }
            }
            if(uiState.value.selectedDateFilter == DateFormat.CUSTOM_RANGE){
                val sales = uiState.value.filteredSales
                val filteredSales = sales.filter{
                    val beforeFlag = uiState.value.selectedDateRange.first?.let { date -> it.createdAt >= date } ?: true
                    val afterFlag = uiState.value.selectedDateRange.second?.let { date -> it.createdAt <= date } ?: true
                    beforeFlag && afterFlag
                }
                _uiState.update { it.copy(filteredSales = filteredSales) }
            } else {
                convertToDatedSales()
            }
        }
    }

    private fun convertToDatedSales() {
        viewModelScope.launch (Dispatchers.Default){
            convertSalesToDatedSalesUseCase(
                _uiState.value.filteredSales,
                _uiState.value.selectedDateFilter
            ).let{
                _uiState.update { oldState ->
                    oldState.copy(datedSales = it)
                }
            }
        }
    }
}