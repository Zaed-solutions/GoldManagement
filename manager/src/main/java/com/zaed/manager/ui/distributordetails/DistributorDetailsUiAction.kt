package com.zaed.manager.ui.distributordetails

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface DistributorDetailsUiAction {
    data object OnBackClicked: DistributorDetailsUiAction
    data class OnSalesQueryChanged(val query: String): DistributorDetailsUiAction
    data class UpdateSalesDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class OnSaleClicked(val saleId: String, val type: String): DistributorDetailsUiAction
    data class OnInventoryQueryChanged(val query: String): DistributorDetailsUiAction
    data class UpdateLossesDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class SetLossesDateRange(val range: Pair<Date?, Date?>): DistributorDetailsUiAction
    data class UpdateIngotTransactionsDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class SetIngotTransactionsDateRange(val range: Pair<Date?, Date?>): DistributorDetailsUiAction
    data class OnSaveInventory(val inventory: Inventory): DistributorDetailsUiAction
    data class SetSalesDateRange(val range: Pair<Date?, Date?>): DistributorDetailsUiAction
}