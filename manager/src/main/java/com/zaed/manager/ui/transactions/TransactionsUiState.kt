package com.zaed.manager.ui.transactions

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.WholesaleTransaction

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val currentUser: User = User(),
    val searchQuery: String = "",
    val allPurchases: List<WholesaleTransaction> = emptyList(),
    val displayedPurchases: List<WholesaleTransaction> = emptyList(),
    val allSales: List<WholesaleTransaction> = emptyList(),
    val displayedSales: List<WholesaleTransaction> = emptyList()
)
