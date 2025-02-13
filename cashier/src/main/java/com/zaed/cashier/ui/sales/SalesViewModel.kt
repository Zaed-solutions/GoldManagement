package com.zaed.cashier.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.domain.FetchStoreSalesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(
    private val fetchStoreSalesUseCase: FetchStoreSalesUseCase
) : ViewModel() {
    private val TAG: String = "SalesViewModel"
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchSales()
    }

    private fun fetchSales() {
        //todo: initialize storeId
        viewModelScope.launch(Dispatchers.IO) {
            fetchStoreSalesUseCase(
                FetchStoreSalesRequest(
                    storeId = uiState.value.storeId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { it.copy(isLoading = false, sales = data) }
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
            else -> Unit
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
            filterData(query)
        }
    }

    private fun filterData(
        query: String = uiState.value.searchQuery
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            with(uiState.value.sales) {
                if (query.isBlank()) {
                    _uiState.update { it.copy(displaySales = this) }
                } else {
                    val filteredSales =
                        filter { it.customerName.contains(query, ignoreCase = true) }
                    _uiState.update { it.copy(displaySales = filteredSales) }
                }
            }
        }
    }
}