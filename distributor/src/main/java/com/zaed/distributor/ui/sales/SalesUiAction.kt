package com.zaed.distributor.ui.sales

sealed interface SalesUiAction {
    data object AddSaleClicked: SalesUiAction
    data object OnSignOut: SalesUiAction
    data class UpdateSearchQuery(val searchQuery: String): SalesUiAction
    data class UpdatePaymentStatusFilter(val status: PaymentStatus): SalesUiAction
    data class OnDeleteSale(val saleId: String): SalesUiAction
    data class OnEditSale(val saleId: String): SalesUiAction
    data class OnSaleClicked(val saleId: String): SalesUiAction
}