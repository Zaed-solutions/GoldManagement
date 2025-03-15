package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.util.DateFormat

data class SalesUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val selectedDateFilter: DateFormat = DateFormat.DATE,
    val allSales: List<StoreTransaction> = emptyList(),
    val filteredSales: List<StoreTransaction> = emptyList(),
    val datedSales: List<DatedSales> = emptyList(),
)
