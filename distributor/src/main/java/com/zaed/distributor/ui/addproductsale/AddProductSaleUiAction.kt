package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.Product

sealed interface AddProductSaleUiAction {
    data object OnBackClicked : AddProductSaleUiAction
    data object OnSubmitClicked : AddProductSaleUiAction
    data object OnAddNewCustomerClicked : AddProductSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddProductSaleUiAction
    data class OnDeleteProduct(val product: Product) : AddProductSaleUiAction
    data class OnAddProduct(val product: Product) : AddProductSaleUiAction
    data class OnEditProduct(val product: Product) : AddProductSaleUiAction
    data class OnCustomerSearchQueryChanged(val query: String) : AddProductSaleUiAction
    data class OnCustomerSelected(val customer: WholeSaleCustomer) : AddProductSaleUiAction
    data class OnAddPayment(val cashPayment: Payment): AddProductSaleUiAction
    data class OnEditPayment(val cashPayment: Payment): AddProductSaleUiAction
    data class OnRemovePayment(val paymentId: String): AddProductSaleUiAction
    data class OnUpdateProducts(val products: List<Product>): AddProductSaleUiAction
    data object OnDeleteAllProducts: AddProductSaleUiAction

}