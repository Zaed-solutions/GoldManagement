package com.zaed.manager.ui.salescheques

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.supplier.Supplier

sealed interface SalesChequesUiAction {
    data object OnBackClicked : SalesChequesUiAction
    data object OnDrawerClicked : SalesChequesUiAction
    data class DeletePayment(val cashPayment: Payment) : SalesChequesUiAction
    data object OnAddNewCustomer : SalesChequesUiAction
    data class OnQueryChanged(val query: String) : SalesChequesUiAction
    data class OnCustomerSelected(val customer: WholeSaleCustomer) : SalesChequesUiAction
    data class OnAddPayment(val payment: Payment) : SalesChequesUiAction
    data class OnEditPayment(val oldPayment: Payment, val newPayment: Payment) : SalesChequesUiAction
    data class OnUpdateSearchQuery(val query: String) : SalesChequesUiAction
    data class OnSupplierClicked(val supplierId: String) : SalesChequesUiAction
    data class OnAddSupplier(val supplier: Supplier) : SalesChequesUiAction
}