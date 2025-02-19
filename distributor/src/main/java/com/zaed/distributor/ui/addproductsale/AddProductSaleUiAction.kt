package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product

sealed interface AddProductSaleUiAction {
    data object OnBackClicked : AddProductSaleUiAction
    data object OnSubmitClicked : AddProductSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddProductSaleUiAction
    data class OnAddProduct(val product: Product) : AddProductSaleUiAction
    data class OnEditProduct(val product: Product) : AddProductSaleUiAction
}