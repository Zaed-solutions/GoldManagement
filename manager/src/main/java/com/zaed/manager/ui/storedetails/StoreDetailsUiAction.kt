package com.zaed.manager.ui.storedetails

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface StoreDetailsUiAction {
    data object OnBackClicked: StoreDetailsUiAction
    data object OnDeleteStore: StoreDetailsUiAction
    data class OnInventoryQueryChanged(val query: String): StoreDetailsUiAction
    data class OnSalesQueryChanged(val query: String): StoreDetailsUiAction
    data class UpdateSalesDateFilter(val format: DateFormat): StoreDetailsUiAction
    data class SetSalesDateRange(val range: Pair<Date?, Date?>): StoreDetailsUiAction
    data class OnUpdateStore(val store: Store): StoreDetailsUiAction
    data class OnSaleClicked(val id: String): StoreDetailsUiAction
    data class UpdateLossesDateFilter(val format: DateFormat): StoreDetailsUiAction
    data class SetLossesDateRange(val range: Pair<Date?, Date?>): StoreDetailsUiAction
    data class OnSaveInventory(val inventory: Inventory): StoreDetailsUiAction
}