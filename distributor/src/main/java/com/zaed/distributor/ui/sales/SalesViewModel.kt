package com.zaed.distributor.ui.sales

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SalesViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: SalesUiAction){
        when(action){
            is SalesUiAction.OnDeleteSale -> TODO()
            SalesUiAction.OnSignOut -> TODO()
            is SalesUiAction.UpdatePaymentStatusFilter -> TODO()
            is SalesUiAction.UpdateSearchQuery -> TODO()
            else -> Unit
        }
    }

    fun filterData(
        searchQuery: String = uiState.value.searchQuery,
        paymentStatus: PaymentStatus = uiState.value.selectedPaymentStatus
    ) {


    }    }