package com.zaed.manager.ui.storedetails

import com.zaed.common.data.model.Inventory
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.util.DateFormat

data class StoreDetailsUiState(
    val isLoading: Boolean = true,
    val store: Store = Store(),
    val allInventories: List<Inventory> = emptyList(),
    val displayedInventories: List<Inventory> = emptyList(),
    val selectedFilter: DateFormat = DateFormat.DATE,
    val inventoryQuery: String = "",
    val salesQuery: String = "",
    val datedSales: List<DatedSales> = emptyList(),
    val displayedDatedSales: List<DatedSales> = emptyList()
)
