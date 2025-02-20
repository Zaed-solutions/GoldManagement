package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.WholeSaleCustomer

sealed interface AddProductSaleUiAction {
    data object OnBackClicked : AddProductSaleUiAction
    data object OnSubmitClicked : AddProductSaleUiAction
    data object OnAddNewCustomerClicked : AddProductSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddProductSaleUiAction
    data class OnAddProduct(val product: Product) : AddProductSaleUiAction
    data class OnEditProduct(val product: Product) : AddProductSaleUiAction
    data class OnCustomerSearchQueryChanged(val query: String) : AddProductSaleUiAction
    data class OnCustomerSelected(val customer: WholeSaleCustomer) : AddProductSaleUiAction
}