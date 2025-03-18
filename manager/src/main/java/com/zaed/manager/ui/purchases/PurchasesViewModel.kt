package com.zaed.manager.ui.purchases

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import com.zaed.common.domain.purchase.DeletePurchaseUseCase
import com.zaed.common.domain.purchase.FetchPurchasesUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchasesViewModel(
    private val fetchPurchasesUseCase: FetchPurchasesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val deletePurchaseUseCase: DeletePurchaseUseCase,
    private val convertDateFormatUseCase: ConvertSalesToDatedSalesUseCase,
    private val logOutUseCase: LogoutUserUseCase
) : ViewModel() {
    private val TAG = "PurchasesViewModel"
    private val _uiState = MutableStateFlow(PurchasesUiState())
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
                    fetchPurchases()
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchPurchases() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPurchasesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allPurchases = data,
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

    fun handleAction(action: PurchasesUiAction) {
        when (action) {
            is PurchasesUiAction.OnDeletePurchases -> deleteProductSale(action.purchaseId)
            PurchasesUiAction.OnSignOut -> signOut()
            is PurchasesUiAction.UpdateDateFilter -> updateDateFilter(action.filter)
            is PurchasesUiAction.UpdateSearchQuery -> updateSearchQuery(action.searchQuery)
            else -> Unit
        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            logOutUseCase().onSuccess {
                _uiState.update { it.copy(isSignedOut = true) }
                Log.d(TAG, "signOut: success")
            }.onFailure {
                Log.e(TAG, "signOut: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun deleteProductSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePurchaseUseCase(
                DeleteWholesaleRequest(
                    id = saleId,
                    distributorId = uiState.value.currentUser.id,
                    distributorName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteProductSale: success")
            }.onFailure {
                Log.e(TAG, "deleteProductSale: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun updateSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, searchQuery = searchQuery)
            }
            filterData(searchQuery = searchQuery)
        }
    }

    private fun updateDateFilter(dateFilter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, dateFilter = dateFilter)
            }
            convertToDatedSales()
        }
    }

    private fun filterData(
        searchQuery: String = uiState.value.searchQuery,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            if (searchQuery.isBlank()) {
                //no search query and payment status is all
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, filteredPurchases = oldState.allPurchases)
                }
            } else {
                //search query is not blank & payment status is not all
                val filteredSales = uiState.value.allPurchases.filter {
                    listOf(
                        it.customerName,
                        it.receiptNumber
                    ).any {
                        it.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                }
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, filteredPurchases = filteredSales)
                }
            }
            convertToDatedSales()
        }

    }

    private fun convertToDatedSales() {
        viewModelScope.launch(Dispatchers.Default) {
            val datedSales =
                convertDateFormatUseCase(uiState.value.filteredPurchases, uiState.value.dateFilter)
            _uiState.update { oldState ->
                oldState.copy(isLoading = false, datedSales = datedSales)
            }
        }
    }
}