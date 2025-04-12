package com.zaed.manager.ui.storesoverview

import com.zaed.common.data.model.store.Store
import com.zaed.manager.ui.storessales.components.StoreSalesFilter

sealed interface StoresOverviewUiAction {
    data object OnBackClicked: StoresOverviewUiAction
    data class OnAddStore(val store: Store): StoresOverviewUiAction
    data class OnUpdateStore(val store: Store): StoresOverviewUiAction
    data class OnDeleteStore(val store: Store): StoresOverviewUiAction
    data class OnStoreClicked(val storeId: String): StoresOverviewUiAction
    data class UpdateSalesSearchQuery(val query: String) : StoresOverviewUiAction
    data class UpdateSalesFilter(val filter: StoreSalesFilter) : StoresOverviewUiAction
    data class OnSaleClicked(val saleId: String) : StoresOverviewUiAction
}