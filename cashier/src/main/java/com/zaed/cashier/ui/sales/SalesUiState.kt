package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.util.DateFormat
import java.util.Date

data class SalesUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val selectedDateFilter: DateFormat = DateFormat.DATE,
    val allSales: List<StoreSale> = emptyList(),
    val filteredSales: List<StoreSale> = emptyList(),
    val datedSales: List<DatedSales> = emptyList(),
)
