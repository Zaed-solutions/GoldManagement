package com.zaed.distributor.ui.addGoldSale

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.Product

sealed interface AddGoldSaleUiAction {
    data object OnBackClicked : AddGoldSaleUiAction
    data object OnSubmitClicked : AddGoldSaleUiAction
    data object OnAddNewCustomerClicked : AddGoldSaleUiAction
    data class OnRemoveProduct(val productId: String) : AddGoldSaleUiAction
    data class OnDeleteProduct(val product: Product) : AddGoldSaleUiAction
    data class OnAddProduct(val product: Product) : AddGoldSaleUiAction
    data class OnEditProduct(val product: Product) : AddGoldSaleUiAction
    data class OnCustomerSearchQueryChanged(val query: String) : AddGoldSaleUiAction
    data class OnCustomerSelected(val customer: WholeSaleCustomer) : AddGoldSaleUiAction
    data class OnAddPayment(val payment: Payment): AddGoldSaleUiAction
    data class OnEditPayment(val payment: Payment): AddGoldSaleUiAction
    data class OnRemovePayment(val paymentId: String): AddGoldSaleUiAction
    data class OnUpdateProducts(val products: List<Product>): AddGoldSaleUiAction
    data object OnDeleteAllProducts: AddGoldSaleUiAction
}