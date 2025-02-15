package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.User

data class SalesUiState(
    val isLoading: Boolean = true,
    val sales: List<StoreSale> = emptyList(),
    val searchQuery: String = "",
    val currentUser: User = User(),
    val displaySales: List<StoreSale> = emptyList()
)
