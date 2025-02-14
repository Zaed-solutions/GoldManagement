package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product

sealed interface AddSaleUiAction {
    data object OnBackClicked : AddSaleUiAction
    data object OnAddClicked : AddSaleUiAction
    data class OnUpdateCustomerName(val customerName: String) : AddSaleUiAction
    data class OnUpdateCustomerPhone(val customerPhoneNumber: String) : AddSaleUiAction
    data class OnUpdateCustomerEmail(val customerEmail: String) : AddSaleUiAction
    data class OnUpdateProductQuantity(val productId: String, val quantity: Int) : AddSaleUiAction
    data class OnUpdateProductPrice(val productId: String, val price: Double) : AddSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddSaleUiAction
    data class OnAddProduct(val product: Product) : AddSaleUiAction
    data class OnUpdateDiscountType(val discountType: DiscountType) : AddSaleUiAction
    data class OnUpdateDiscountValue(val discountValue: Double) : AddSaleUiAction
}