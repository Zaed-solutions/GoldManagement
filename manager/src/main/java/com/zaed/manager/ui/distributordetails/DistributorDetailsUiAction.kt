package com.zaed.manager.ui.distributordetails

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.ui.util.DateFormat

sealed interface DistributorDetailsUiAction {
    data object OnBackClicked: DistributorDetailsUiAction
    data class OnSalesQueryChanged(val query: String): DistributorDetailsUiAction
    data class UpdateSalesDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class OnSaleClicked(val saleId: String): DistributorDetailsUiAction
    data class OnInventoryQueryChanged(val query: String): DistributorDetailsUiAction
    data class UpdateLossesDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class UpdateIngotTransactionsDateFilter(val dateFormat: DateFormat): DistributorDetailsUiAction
    data class OnSaveInventory(val inventory: Inventory): DistributorDetailsUiAction
}