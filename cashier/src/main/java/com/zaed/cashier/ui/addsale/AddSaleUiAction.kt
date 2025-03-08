package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.sale.Product

sealed interface AddSaleUiAction {
    data object OnShowNavDrawer : AddSaleUiAction
    data object OnSubmitClicked : AddSaleUiAction
    data class OnDeleteProduct(val product: Product) : AddSaleUiAction
    data class OnAddProduct(val product: Product) : AddSaleUiAction
    data class OnUpdateProduct(val product: Product) : AddSaleUiAction
    data class OnUpdateCustomerName(val name: String) : AddSaleUiAction
    data class OnUpdateCustomerPhone(val phone: String) : AddSaleUiAction
    data class OnUpdateCustomerEmail(val email: String) : AddSaleUiAction
    data object OnDeleteAllProducts: AddSaleUiAction
}