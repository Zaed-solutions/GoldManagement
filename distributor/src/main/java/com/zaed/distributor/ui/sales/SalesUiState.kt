package com.zaed.distributor.ui.sales

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.WholesaleSale

data class SalesUiState(
    val isLoading: Boolean = true,
    val isSignedOut: Boolean = false,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val selectedPaymentStatus: PaymentStatus = PaymentStatus.ALL,
    val allSales: List<WholesaleSale> = emptyList(),
    val displayedSales: List<WholesaleSale> = emptyList()
)
