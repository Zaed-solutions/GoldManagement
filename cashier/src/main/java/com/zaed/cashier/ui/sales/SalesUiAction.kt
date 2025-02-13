package com.zaed.cashier.ui.sales

sealed interface SalesUiAction {
    data object AddSaleClicked: SalesUiAction
    data class UpdateSearchQuery(val query: String): SalesUiAction
    data class OnSaleClicked(val saleId: String): SalesUiAction
}