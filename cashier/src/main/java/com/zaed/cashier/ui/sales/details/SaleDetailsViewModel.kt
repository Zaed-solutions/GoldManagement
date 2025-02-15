package com.zaed.cashier.ui.sales.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.StoreSale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SaleDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SaleDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: SaleDetailsUiAction) {
        when (action) {
            is SaleDetailsUiAction.OnUpdateStoreSale -> updateStoreSale(action.storeSale)
            SaleDetailsUiAction.OnSubmit -> submit()
            SaleDetailsUiAction.ResetError -> resetError()
            else -> {}
        }
    }

    private fun updateStoreSale(storeSale: StoreSale) {
        _uiState.update { it.copy(storeSale = storeSale) }
    }

    private fun submit() {
        if (!validateInput()) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            // Call your use case or repository here
        }
    }

    private fun validateInput(): Boolean {
        return true
    }

    private fun resetError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}