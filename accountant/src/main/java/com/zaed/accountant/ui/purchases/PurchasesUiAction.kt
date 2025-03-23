package com.zaed.accountant.ui.purchases

sealed interface PurchasesUiAction {
    data object OnShowNavDrawer: PurchasesUiAction
    data class OnPurchaseClicked(val purchaseId: String): PurchasesUiAction
    data class OnSearchQueryChanged(val query: String): PurchasesUiAction
}