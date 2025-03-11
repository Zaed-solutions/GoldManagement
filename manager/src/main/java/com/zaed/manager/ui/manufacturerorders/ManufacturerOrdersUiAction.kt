package com.zaed.manager.ui.manufacturerorders

import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.manager.ui.manufacturerorders.components.ManufacturerOrdersFilter

sealed interface ManufacturerOrdersUiAction {
    data object ShowNavDrawer: ManufacturerOrdersUiAction
    data class UpdateSearchQuery(val query: String) : ManufacturerOrdersUiAction
    data class SaveManufacturerOrder(val order: ManufacturerOrder) : ManufacturerOrdersUiAction
    data class DeleteManufacturerOrder(val order: ManufacturerOrder) : ManufacturerOrdersUiAction
    data class UpdateFilter(val filter: ManufacturerOrdersFilter) : ManufacturerOrdersUiAction
}