package com.zaed.distributor.ui.productsaledetails

sealed interface ProductSaleDetailsUiAction {
    data object OnBackClicked: ProductSaleDetailsUiAction
    data object OnEditClicked: ProductSaleDetailsUiAction
    data object OnDeleteSale: ProductSaleDetailsUiAction
    data object OnPrintReceipt: ProductSaleDetailsUiAction
    data object OnShareReceiptViaWhatsapp: ProductSaleDetailsUiAction
    data object OnShareReceiptViaEmail: ProductSaleDetailsUiAction
    data object OnRequestReceipt: ProductSaleDetailsUiAction
}