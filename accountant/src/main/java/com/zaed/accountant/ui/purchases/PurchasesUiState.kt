package com.zaed.accountant.ui.purchases

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.WholesaleTransaction

data class PurchasesUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val allPurchases: List<WholesaleTransaction> = emptyList(),
    val displayedPurchases: List<WholesaleTransaction> = emptyList(),
)
