package com.zaed.manager.ui.storedetails

import com.zaed.common.data.model.Inventory
import com.zaed.common.data.model.store.Store

data class StoreDetailsUiState(
    val isLoading: Boolean = true,
    val store: Store = Store(),
    val allInventories: List<Inventory> = emptyList(),
    val displayedInventories: List<Inventory> = emptyList(),
    val query: String = ""
)
