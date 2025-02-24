package com.zaed.distributor.ui.ingottransactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.Karat
import com.zaed.common.data.model.sale.TransactionType
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.sale.AddIngotTransactionUseCase
import com.zaed.common.domain.sale.FetchIngotTransactionsUseCase
import com.zaed.common.domain.sale.UpdateIngotTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class IngotTransactionsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val fetchTransactionUseCase: FetchIngotTransactionsUseCase,
    private val addTransactionUseCase: AddIngotTransactionUseCase,
    private val updateTransactionUseCase: UpdateIngotTransactionUseCase
) : ViewModel() {
    private val TAG = "AddIngotTransactionViewModel"
    private val _uiState = MutableStateFlow(IngotTransactionsUiState())
    val uiState = _uiState.asStateFlow()

    init{
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                    fetchTransactions(data.id)
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
                Log.d(
                    "find the issue",
                    "fetchCurrentUser: $result"
                )
            }
        }
    }

    private fun fetchTransactions(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchTransactionUseCase(
                FetchIngotTransactionsRequest(
                    distributorId = distributorId
                )
            ).collect { result ->
                result.onSuccess { data ->
                    val groupedTransaction = data.sortedByDescending { it.createdAt }.groupBy { it.type }
                    _uiState.update { oldState->
                        oldState.copy(
                            allSaleTransactions = groupedTransaction[TransactionType.SALE] ?: emptyList(),
                            allPurchaseTransactions = groupedTransaction[TransactionType.PURCHASE] ?: emptyList(),
                            isLoading = false
                        )
                    }
                    filterData()
                }.onFailure {
                    Log.e(TAG, "fetchTransaction: ${it.message}", it)
                }
            }
        }
    }

    fun handleAction(action: IngotTransactionsUiAction) {
        when (action) {
            is IngotTransactionsUiAction.OnSaveTransaction -> onSave(action.transaction)
            is IngotTransactionsUiAction.OnDeleteTransaction -> deleteTransaction(action.transaction)
            is IngotTransactionsUiAction.OnUpdateSearchQuery -> updateSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(searchQuery = query)
            }
            filterData(query)
        }
    }

    private fun filterData(
        query: String = uiState.value.searchQuery
    ) {
        viewModelScope.launch (Dispatchers.Default){
            if(query.isBlank()){
                _uiState.update { oldState ->
                    oldState.copy(
                        displayedSaleTransactions = uiState.value.allSaleTransactions,
                        displayedPurchaseTransactions = uiState.value.allPurchaseTransactions
                    )
                }
            } else {
                //todo filter data based on query
                _uiState.update { oldState ->
                    oldState.copy(
                        displayedPurchaseTransactions = uiState.value.allSaleTransactions,
                        displayedSaleTransactions = uiState.value.allPurchaseTransactions
                    )
                }
            }
        }
    }

    private fun onSave(transaction: IngotTransaction) {
        if(transaction.id.isNotBlank()) {
            updateTransaction(transaction)
        } else {
            addTransaction(transaction)
        }
    }

    private fun addTransaction(transaction: IngotTransaction) {
        viewModelScope.launch (Dispatchers.IO){
            val logs = listOf(
                ChangeLog(
                    employeeId = _uiState.value.currentUser.id,
                    employeeName = _uiState.value.currentUser.fullName,
                    action = "created this transaction"
                )
            )
            addTransactionUseCase(
                AddIngotTransactionRequest(
                    transaction = transaction.copy(
                        logs = logs,
                        createdAt = Date(),
                        distributorId = _uiState.value.currentUser.id,
                        distributorName = _uiState.value.currentUser.fullName,
                    )
                )
            ).onSuccess { id ->
                Log.d(TAG, "addTransaction: success")
            }.onFailure {
                Log.e(TAG, "addTransaction: ${it.message}", it)
            }
        }
    }

    private fun updateTransaction(transaction: IngotTransaction) {
        viewModelScope.launch (Dispatchers.IO){
            val updateLog = ChangeLog(
                employeeId = _uiState.value.currentUser.id,
                employeeName = _uiState.value.currentUser.fullName,
                action = "updated this transaction"
            )
            updateTransactionUseCase(
                UpdateIngotTransactionRequest(
                    transaction = transaction.copy(
                        logs = transaction.logs + updateLog
                    )
                )
            ).onSuccess {
                Log.d(TAG, "updateTransaction: success")
            }.onFailure {
                Log.e(TAG, "updateTransaction: ${it.message}", it)
            }
        }
    }

    private fun deleteTransaction(transaction: IngotTransaction) {
        viewModelScope.launch (Dispatchers.IO){
            val deleteLog = ChangeLog(
                employeeId = _uiState.value.currentUser.id,
                employeeName = _uiState.value.currentUser.fullName,
                action = "deleted this transaction"
            )
            updateTransactionUseCase(
                UpdateIngotTransactionRequest(
                    transaction = transaction.copy(
                        logs = transaction.logs + deleteLog,
                        deleted = true
                    )
                )
            ).onSuccess {
                Log.d(TAG, "deleteTransaction: success")
            }.onFailure {
                Log.e(TAG, "updateTransaction: ${it.message}", it)
            }
        }
    }
}