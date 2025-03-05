package com.zaed.manager.ui.storedetails

import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.util.DateFormat

sealed interface StoreDetailsUiAction {
    data object OnBackClicked: StoreDetailsUiAction
    data object OnDeleteStore: StoreDetailsUiAction
    data class OnInventoryQueryChanged(val query: String): StoreDetailsUiAction
    data class OnSalesQueryChanged(val query: String): StoreDetailsUiAction
    data class UpdateSalesDateFilter(val format: DateFormat): StoreDetailsUiAction
    data class OnUpdateStore(val store: Store): StoreDetailsUiAction
    data class OnSaleClicked(val id: String): StoreDetailsUiAction
    data class UpdateLossesDateFilter(val format: DateFormat): StoreDetailsUiAction
}