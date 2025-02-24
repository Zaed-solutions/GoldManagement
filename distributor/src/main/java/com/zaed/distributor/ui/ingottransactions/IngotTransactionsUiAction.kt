package com.zaed.distributor.ui.ingottransactions

import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.Karat
import com.zaed.common.data.model.sale.TransactionType

sealed interface IngotTransactionsUiAction {
    data object OnShowNavDrawer: IngotTransactionsUiAction
    data class OnUpdateSearchQuery(val query: String): IngotTransactionsUiAction
    data class OnSaveTransaction(val transaction: IngotTransaction): IngotTransactionsUiAction
    data class OnDeleteTransaction(val transaction: IngotTransaction): IngotTransactionsUiAction
}