package com.zaed.distributor.ui.sales

sealed interface SalesUiAction {
    data object AddProductSaleClicked: SalesUiAction
    data object AddGoldSaleClicked: SalesUiAction
    data object OnSignOut: SalesUiAction
    data class UpdateSearchQuery(val searchQuery: String): SalesUiAction
    data class UpdatePaymentStatusFilter(val status: PaymentStatus): SalesUiAction
    data class OnDeleteProductSale(val saleId: String): SalesUiAction
    data class OnDeleteGoldSale(val saleId: String): SalesUiAction
    data class OnEditProductSale(val saleId: String): SalesUiAction
    data class OnEditGoldSale(val saleId: String): SalesUiAction
    data class OnProductSaleClicked(val saleId: String): SalesUiAction
    data class OnGoldSaleClicked(val saleId: String): SalesUiAction
}