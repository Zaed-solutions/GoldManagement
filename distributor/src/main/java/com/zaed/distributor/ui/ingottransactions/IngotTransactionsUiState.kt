package com.zaed.distributor.ui.ingottransactions

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.util.DateFormat

data class IngotTransactionsUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val allTransactions: List<IngotTransaction> = emptyList(),
    val datedTransactions: List<DatedIngotTransactions> = emptyList(),
    val dateFilter: DateFormat = DateFormat.DATE,


    )
