package com.zaed.manager.ui.manufacturerorders

import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.manager.ui.manufacturerorders.components.ManufacturerOrdersFilter

data class ManufacturerOrdersUiState(
    val isLoading: Boolean = true,
    val allOrders: List<ManufacturerOrder> = emptyList(),
    val displayedOrders: List<ManufacturerOrder> = emptyList(),
    val searchQuery: String = "",
    val filter: ManufacturerOrdersFilter = ManufacturerOrdersFilter()
)
