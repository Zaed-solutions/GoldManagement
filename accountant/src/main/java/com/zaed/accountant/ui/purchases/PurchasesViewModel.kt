package com.zaed.accountant.ui.purchases

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.domain.purchase.FetchPurchasesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchasesViewModel(
    private val fetchPurchasesUseCase: FetchPurchasesUseCase,
) : ViewModel() {
    private val TAG: String = "PurchasesViewModel"
    private val _uiState = MutableStateFlow(PurchasesUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchPurchases()
    }
    private fun fetchPurchases() {
        viewModelScope.launch (Dispatchers.IO){
            fetchPurchasesUseCase().collect{ result ->
                result.onSuccess { data ->
                    Log.d(TAG, "fetchPurchases: $data")
                    _uiState.update { oldState ->
                        oldState.copy(
                            allPurchases = data
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchPurchases: ${e.message}", e)
                }
            }
        }
    }
    private fun filterData() {
        viewModelScope.launch (Dispatchers.Default){
            val query = uiState.value.searchQuery
            val filteredPurchases = uiState.value.allPurchases.filter {
                it.receiptNumber.contains(query)
            }
            _uiState.update {
                it.copy(
                    displayedPurchases = filteredPurchases,
                    isLoading = false
                )
            }
        }
    }
    fun handleAction(action: PurchasesUiAction){
        when(action){
            is PurchasesUiAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            else -> Unit
        }
    }
    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(searchQuery = query)
            }
            filterData()
        }
    }
}