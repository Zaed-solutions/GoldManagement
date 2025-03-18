package com.zaed.manager.ui.purchases

import com.zaed.common.ui.util.DateFormat

sealed interface PurchasesUiAction {
    data object AddProductPurchasesClicked: PurchasesUiAction
    data object AddGoldPurchasesClicked: PurchasesUiAction
    data object OnSignOut: PurchasesUiAction
    data object OnShowNavDrawer: PurchasesUiAction
    data class UpdateSearchQuery(val searchQuery: String): PurchasesUiAction
    data class UpdateDateFilter(val filter: DateFormat): PurchasesUiAction
    data class OnDeletePurchases(val purchaseId: String): PurchasesUiAction
    data class OnEditProductPurchases(val purchaseId: String): PurchasesUiAction
    data class OnEditGoldPurchases(val purchaseId: String): PurchasesUiAction
    data class OnProductPurchasesClicked(val purchaseId: String): PurchasesUiAction
    data class OnGoldPurchasesClicked(val purchaseId: String): PurchasesUiAction
}