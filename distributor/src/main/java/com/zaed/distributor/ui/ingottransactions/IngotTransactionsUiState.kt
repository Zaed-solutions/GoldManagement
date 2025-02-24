package com.zaed.distributor.ui.ingottransactions

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.IngotTransaction

data class IngotTransactionsUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val allPurchaseTransactions: List<IngotTransaction> = emptyList(),
    val displayedPurchaseTransactions: List<IngotTransaction> = emptyList(),
    val allSaleTransactions: List<IngotTransaction> = emptyList(),
    val displayedSaleTransactions: List<IngotTransaction> = emptyList()
)
