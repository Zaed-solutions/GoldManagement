package com.zaed.common.ui.ingottransactions

import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface IngotTransactionsUiAction {
    data object OnShowNavDrawer: IngotTransactionsUiAction
    data class OnSaveTransaction(val transaction: IngotTransaction): IngotTransactionsUiAction
    data class OnDeleteTransaction(val transaction: IngotTransaction): IngotTransactionsUiAction
    data class UpdateIngotTransactionsDateFilter(val dateFormat: DateFormat) :
        IngotTransactionsUiAction
    data class SetCustomRange(val range: Pair<Date?, Date?>) : IngotTransactionsUiAction
}