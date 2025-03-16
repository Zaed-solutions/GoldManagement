package com.zaed.manager.ui.transactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.domain.purchase.FetchPurchasesUseCase
import com.zaed.common.domain.purchase.UpdatePurchaseUseCase
import com.zaed.common.domain.sale.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.FetchDistributorSalesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val deleteGoldSaleUseCase: DeleteWholesaleGoldSaleUseCase,
    private val fetchSalesUseCase: FetchDistributorSalesUseCase,
    private val fetchPurchasesUseCase: FetchPurchasesUseCase,
    private val updatePurchaseUseCase: UpdatePurchaseUseCase
): ViewModel() {
    private val TAG: String = "TransactionsViewModel"
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchSales()
        fetchPurchases()
    }

    private fun fetchPurchases() {
        viewModelScope.launch (Dispatchers.IO){
            fetchPurchasesUseCase().collect{ result ->
                result.onSuccess { data ->
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

    private fun fetchSales() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSalesUseCase(
                FetchDistributorSalesRequest(
                    distributorId = uiState.value.currentUser.id
                )
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data,
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

    fun handleAction(action: TransactionsUiAction) {
        when(action) {
            is TransactionsUiAction.OnDeletePurchase -> deletePurchase(action.purchaseId)
            is TransactionsUiAction.OnDeleteSale -> deleteSale(action.saleId)
            is TransactionsUiAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun deleteSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteGoldSaleUseCase(
                DeleteWholesaleGoldSaleRequest(
                    saleId = saleId,
                    distributorId = uiState.value.currentUser.id,
                    distributorName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteGoldSale: success")
            }.onFailure {
                Log.e(TAG, "deleteGoldSale: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun deletePurchase(purchaseId: String) {
        viewModelScope.launch (Dispatchers.IO){
            val purchase = uiState.value.allPurchases.find { it.id == purchaseId }?: return@launch
            val currentEmployee = uiState.value.currentUser
            updatePurchaseUseCase(
                UpdatePurchaseRequest(
                    purchase = purchase.copy(
                        deleted = true,
                        logs = purchase.logs + ChangeLog(employeeName = currentEmployee.fullName, employeeId = currentEmployee.id, type = LogType.DELETE)
                    ),
                    payments = emptyList(),
                    employeeId = currentEmployee.id,
                    employeeName = currentEmployee.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deletePurchase: success")
            }.onFailure {
                Log.e(TAG, "deletePurchase: ${it.message}", it)
            }
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

    private fun filterData() {
        viewModelScope.launch (Dispatchers.Default){
            val query = uiState.value.searchQuery
            val filteredSales = uiState.value.allSales.filter {
                it.receiptNumber.contains(query)
            }
            val filteredPurchases = uiState.value.allPurchases.filter {
                it.receiptNumber.contains(query)
            }
            _uiState.update {
                it.copy(
                    displayedSales = filteredSales,
                    displayedPurchases = filteredPurchases
                )
            }
        }
    }
}