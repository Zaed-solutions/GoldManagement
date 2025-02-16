package com.zaed.cashier.ui.sales.details

import com.zaed.common.data.model.StoreSale

sealed interface SaleDetailsUiAction {
    data class OnUpdateStoreSale(val storeSale: StoreSale) : SaleDetailsUiAction
    data class Print (val storeSale: StoreSale) : SaleDetailsUiAction
    data class ShareViaWhatsapp (val storeSale: StoreSale) : SaleDetailsUiAction
    data class ShareViaEmail (val storeSale: StoreSale) : SaleDetailsUiAction
    data object OnSubmit : SaleDetailsUiAction
    data object ResetError : SaleDetailsUiAction
    data object OnBack : SaleDetailsUiAction
}