package com.zaed.cashier.ui.sales

import com.zaed.common.data.model.sale.StoreSale

sealed interface SalesUiAction {
    data object AddSaleClicked: SalesUiAction
    data object OnSignOut: SalesUiAction
    data class UpdateSearchQuery(val query: String): SalesUiAction
    data class OnSaleClicked(val saleId: String): SalesUiAction
    data class OnDeleteSale(val saleId: String): SalesUiAction
    data class OnEditSale(val sale: StoreSale): SalesUiAction
}