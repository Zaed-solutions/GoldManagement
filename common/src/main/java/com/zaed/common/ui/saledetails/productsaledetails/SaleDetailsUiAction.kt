package com.zaed.common.ui.saledetails.productsaledetails

import com.zaed.common.data.model.sale.StoreSale

sealed interface SaleDetailsUiAction {
    data object OnBackClicked: SaleDetailsUiAction
    data object OnEditClicked: SaleDetailsUiAction
    data object OnDeleteSale: SaleDetailsUiAction
    data object OnPrintReceipt: SaleDetailsUiAction
    data object OnShareReceiptViaWhatsapp: SaleDetailsUiAction
    data object OnShareReceiptViaEmail: SaleDetailsUiAction
    data object OnRequestReceipt: SaleDetailsUiAction
    data object OnCustomerClicked: SaleDetailsUiAction

    data class OnUpdateStoreSale(val storeSale: StoreSale) : SaleDetailsUiAction
    data class Print (val storeSale: StoreSale) : SaleDetailsUiAction
    data class ShareViaWhatsapp (val storeSale: StoreSale) : SaleDetailsUiAction
    data class ShareViaEmail (val storeSale: StoreSale) : SaleDetailsUiAction
    data class UpdateCustomerEmail (val email: String) : SaleDetailsUiAction
    data class UpdateCustomerPhoneNumber (val phoneNumber: String) : SaleDetailsUiAction
    data object OnSubmit : SaleDetailsUiAction
    data object ResetError : SaleDetailsUiAction
}