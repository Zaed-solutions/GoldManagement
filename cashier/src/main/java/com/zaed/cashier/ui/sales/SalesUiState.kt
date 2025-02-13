package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.StoreSale

data class SalesUiState(
    val isLoading: Boolean = true,
    val storeId: String = "",
    val sales: List<StoreSale> = emptyList(),
    val searchQuery: String = "",
    val displaySales: List<StoreSale> = emptyList()
)
