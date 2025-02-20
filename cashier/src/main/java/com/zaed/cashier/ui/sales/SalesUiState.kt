package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.authentication.User

data class SalesUiState(
    val isSignedOut: Boolean = false,
    val isLoading: Boolean = true,
    val sales: List<StoreSale> = emptyList(),
    val searchQuery: String = "",
    val currentUser: User = User(),
    val displaySales: List<StoreSale> = emptyList()
)
