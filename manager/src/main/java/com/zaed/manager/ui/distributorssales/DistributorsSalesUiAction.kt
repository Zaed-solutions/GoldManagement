package com.zaed.manager.ui.distributorssales

import com.zaed.manager.ui.distributorssales.components.DistributorsSalesFilter

sealed interface DistributorsSalesUiAction {
    data object OnShowNavDrawer : DistributorsSalesUiAction
    data class OnSaleClicked(val saleId: String) : DistributorsSalesUiAction
    data class UpdateSearchQuery(val query: String) : DistributorsSalesUiAction
    data class UpdateFilter(val filter: DistributorsSalesFilter) : DistributorsSalesUiAction
}