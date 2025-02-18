package com.zaed.distributor.ui.sales

import com.zaed.common.data.model.WholesaleSale

data class SalesUiState(
    val isLoading: Boolean = false,
    val isSignedOut: Boolean = false,
    val searchQuery: String = "",
    val selectedPaymentStatus: PaymentStatus = PaymentStatus.ALL,
    val allSales: List<WholesaleSale> = emptyList(),
    val displayedSales: List<WholesaleSale> = emptyList()
)
