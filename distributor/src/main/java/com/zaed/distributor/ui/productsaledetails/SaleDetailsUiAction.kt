package com.zaed.distributor.ui.productsaledetails

sealed interface SaleDetailsUiAction {
    data object OnBackClicked: SaleDetailsUiAction
    data object OnEditClicked: SaleDetailsUiAction
    data object OnDeleteSale: SaleDetailsUiAction
    data object OnPrintReceipt: SaleDetailsUiAction
    data object OnShareReceiptViaWhatsapp: SaleDetailsUiAction
    data object OnShareReceiptViaEmail: SaleDetailsUiAction
    data object OnRequestReceipt: SaleDetailsUiAction
    data object OnCustomerClicked: SaleDetailsUiAction

}