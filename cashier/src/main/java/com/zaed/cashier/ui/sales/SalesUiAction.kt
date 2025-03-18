package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface SalesUiAction {
    data object AddSaleClicked: SalesUiAction
    data object ShowNavDrawer: SalesUiAction
    data class UpdateSearchQuery(val query: String): SalesUiAction
    data class OnSaleClicked(val saleId: String): SalesUiAction
    data class OnDeleteSale(val saleId: String): SalesUiAction
    data class OnEditSale(val sale: StoreTransaction): SalesUiAction
    data class UpdateSelectedDate(val filter: DateFormat): SalesUiAction
    data class SetCustomRangeFilter(val range: Pair<Date?, Date?>): SalesUiAction
}