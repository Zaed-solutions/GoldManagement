package com.zaed.manager.ui.storedetails

import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.util.DateFormat

data class StoreDetailsUiState(
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val store: Store = Store(),
    val mainInventories: List<Inventory> = emptyList(),
    val allInventories: List<Inventory> = emptyList(),
    val displayedInventories: List<Inventory> = emptyList(),
    val selectedSalesFilter: DateFormat = DateFormat.DATE,
    val inventoryQuery: String = "",
    val salesQuery: String = "",
    val allSales: List<StoreTransaction> = emptyList(),
    val filteredSales: List<StoreTransaction> = emptyList(),
    val datedSales: List<DatedSales> = emptyList(),
    val allLosses: List<Loss> = emptyList(),
    val filteredLosses: List<Loss> = emptyList(),
    val datedLosses: List<DatedLoss> = emptyList(),
    val selectedLossesFilter: DateFormat = DateFormat.DATE
)
