package com.zaed.cashier.ui.addsale

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddSaleViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddSaleUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: AddSaleUiAction){
        when(action){
            AddSaleUiAction.OnAddClicked -> TODO()
            is AddSaleUiAction.OnUpdateCustomerName -> updateCustomerName(action.customerName)
            is AddSaleUiAction.OnUpdateCustomerEmail -> updateCustomerEmail(action.customerEmail)
            is AddSaleUiAction.OnUpdateCustomerPhone -> updateCustomerPhone(action.customerPhoneNumber)
            else -> Unit
        }
    }

    private fun updateCustomerPhone(customerPhoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerPhoneNumber = customerPhoneNumber))
            }
        }
    }

    private fun updateCustomerEmail(customerEmail: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerEmail = customerEmail))
            }
        }
    }

    private fun updateCustomerName(customerName: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerName = customerName))
            }
        }
    }
}