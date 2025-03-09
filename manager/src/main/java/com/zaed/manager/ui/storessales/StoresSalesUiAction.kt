package com.zaed.manager.ui.storessales

import com.zaed.manager.ui.storessales.components.StoreSalesFilter

sealed interface StoresSalesUiAction {
    data object OnShowNavDrawer : StoresSalesUiAction
    data class OnSaleClicked(val saleId: String) : StoresSalesUiAction
    data class UpdateSearchQuery(val query: String) : StoresSalesUiAction
    data class UpdateFilter(val filter: StoreSalesFilter) : StoresSalesUiAction
}