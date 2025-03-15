package com.zaed.manager.ui.transactions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState = _uiState.asStateFlow()
    init {

    }
    fun handleAction(action: TransactionsUiAction) {
        when(action) {
            else -> Unit
        }
    }
}