package com.zaed.cashier.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.domain.DeleteStoreSaleUseCase
import com.zaed.common.domain.FetchStoreSalesUseCase
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.LogoutUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(
    private val fetchStoreSalesUseCase: FetchStoreSalesUseCase,
    private val deleteStoreSaleUseCase: DeleteStoreSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val logOutUseCase: LogoutUserUseCase
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
            is SalesUiAction.OnDeleteSale -> deleteSale(action.saleId)
            SalesUiAction.OnSignOut -> signOut()
            else -> Unit
        }
    }

    private fun deleteSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            deleteStoreSaleUseCase(
                DeleteStoreSaleRequest(
                    saleId = saleId,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteSale: success")
            }.onFailure {e->
                Log.e(TAG, "deleteSale: ${e.message}",e )
                e.printStackTrace()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch (Dispatchers.IO){
            logOutUseCase().onSuccess {
                _uiState.update { it.copy(isSignedOut = true) }
                Log.d(TAG, "signOut: success")
            }.onFailure {
                Log.e(TAG, "signOut: ${it.message}", it)
                it.printStackTrace()
            }
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