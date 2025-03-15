package com.zaed.common.ui.addpurchase

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.supplier.Supplier

sealed interface AddPurchaseUiAction {
    data object OnBackClicked : AddPurchaseUiAction
    data object OnSubmitClicked : AddPurchaseUiAction
    data class OnAddNewSupplierClicked(val supplier: Supplier) : AddPurchaseUiAction
    data object OpenDrawer: AddPurchaseUiAction
    data class OnSupplierSearchQueryChanged(val query: String) : AddPurchaseUiAction
    data class OnSupplierSelected(val supplierId: String) : AddPurchaseUiAction
    data class OnRemoveProduct(val productId: String) : AddPurchaseUiAction
    data class OnDeleteProduct(val product: Product) : AddPurchaseUiAction
    data class OnAddProduct(val product: Product) : AddPurchaseUiAction
    data class OnEditProduct(val product: Product) : AddPurchaseUiAction
    data class OnAddPayment(val cashPayment: Payment): AddPurchaseUiAction
    data class OnEditPayment(val cashPayment: Payment): AddPurchaseUiAction
    data class OnRemovePayment(val paymentId: String): AddPurchaseUiAction
    data class OnAddNewCategory(val category: Category): AddPurchaseUiAction
    data class OnUpdateProducts(val products: List<Product>): AddPurchaseUiAction
    data object OnDeleteAllProducts: AddPurchaseUiAction
    data object OnAddSupplier: AddPurchaseUiAction

}