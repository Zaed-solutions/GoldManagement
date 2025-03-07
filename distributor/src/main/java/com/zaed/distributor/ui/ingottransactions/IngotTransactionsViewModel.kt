package com.zaed.distributor.ui.ingottransactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.sale.AddIngotTransactionUseCase
import com.zaed.common.domain.sale.ConvertIngotTransactionsToDatedUseCase
import com.zaed.common.domain.sale.FetchIngotTransactionsUseCase
import com.zaed.common.domain.sale.UpdateIngotTransactionUseCase
import com.zaed.common.ui.util.DateFormat
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
    private val updateTransactionUseCase: UpdateIngotTransactionUseCase,
    private val convertDataToDatedTransactions: ConvertIngotTransactionsToDatedUseCase
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
                    _uiState.update { oldState->
                        oldState.copy(
                            allTransactions = data,
                        )
                    }
                    convertDataToDatedTransactions()
                }.onFailure {
                    Log.e(TAG, "fetchTransaction: ${it.message}", it)
                }
            }
        }
    }

    private fun convertDataToDatedTransactions() {
        viewModelScope.launch(Dispatchers.Default) {
            convertDataToDatedTransactions(_uiState.value.allTransactions, _uiState.value.dateFilter).let {
                _uiState.update { oldState ->
                    oldState.copy(
                        datedTransactions = it,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun handleAction(action: IngotTransactionsUiAction) {
        when (action) {
            is IngotTransactionsUiAction.OnSaveTransaction -> onSave(action.transaction)
            is IngotTransactionsUiAction.OnDeleteTransaction -> deleteTransaction(action.transaction)
            is IngotTransactionsUiAction.UpdateIngotTransactionsDateFilter -> updateFilter(action.dateFormat)
            else -> Unit
        }
    }

    private fun updateFilter(filter:DateFormat) {
        _uiState.update { oldState ->
            oldState.copy(
                dateFilter = filter
            )
        }
        convertDataToDatedTransactions()
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
                    type = LogType.CREATE
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
                type = LogType.UPDATE
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
                type = LogType.DELETE
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