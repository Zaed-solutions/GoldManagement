package com.zaed.common.ui.addsilversale

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.Product

sealed interface AddSilverSaleUiAction {
    data object OnBackClicked : AddSilverSaleUiAction
    data object OnSubmitClicked : AddSilverSaleUiAction
    data object OnAddNewCustomerClicked : AddSilverSaleUiAction
    data object OpenDrawer: AddSilverSaleUiAction
    data class OnCustomerSearchQueryChanged(val query: String) : AddSilverSaleUiAction
    data class OnCustomerSelected(val customer: WholeSaleCustomer) : AddSilverSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddSilverSaleUiAction
    data class OnDeleteProduct(val product: Product) : AddSilverSaleUiAction
    data class OnAddProduct(val product: Product) : AddSilverSaleUiAction
    data class OnEditProduct(val product: Product) : AddSilverSaleUiAction
    data class OnAddPayment(val cashPayment: Payment): AddSilverSaleUiAction
    data class OnEditPayment(val cashPayment: Payment): AddSilverSaleUiAction
    data class OnRemovePayment(val paymentId: String): AddSilverSaleUiAction
    data class OnUpdateProducts(val products: List<Product>): AddSilverSaleUiAction
    data class OnUpdateDiscount(val discount: Double): AddSilverSaleUiAction
    data object OnDeleteAllProducts: AddSilverSaleUiAction
}