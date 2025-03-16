package com.zaed.manager.ui.transactions

sealed interface TransactionsUiAction {
    data object OnShowNavDrawer: TransactionsUiAction
    data class OnSaleClicked(val saleId: String): TransactionsUiAction
    data class OnPurchaseClicked(val purchaseId: String): TransactionsUiAction
    data class OnEditSaleClicked(val saleId: String): TransactionsUiAction
    data class OnEditPurchaseClicked(val purchaseId: String): TransactionsUiAction
    data class OnDeleteSale(val saleId: String): TransactionsUiAction
    data class OnDeletePurchase(val purchaseId: String): TransactionsUiAction
    data class OnSearchQueryChanged(val query: String): TransactionsUiAction
}