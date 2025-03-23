package com.zaed.common.ui.salescheques

import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.supplier.Supplier

sealed interface SalesChequesUiAction {
    data object OnBackClicked : SalesChequesUiAction
    data object OnDrawerClicked : SalesChequesUiAction
    data class DeletePayment(val cashPayment: Payment) : SalesChequesUiAction
    data object OnAddNewCustomer : SalesChequesUiAction
    data class OnCustomerSearchQueryChanged(val query: String) : SalesChequesUiAction
    data class OnAccountSelected(val account: Account) : SalesChequesUiAction
    data class OnAddPayment(val payment: Payment, val isSupplier: Boolean) : SalesChequesUiAction
    data class OnEditPayment(val oldPayment: Payment, val newPayment: Payment, val isSupplier: Boolean) :
        SalesChequesUiAction
    data class OnUpdateSupplierSearchQuery(val query: String) : SalesChequesUiAction
    data class OnUpdateChequeSearchQuery(val query: String) : SalesChequesUiAction
    data class OnAddSupplier(val supplier: Supplier) : SalesChequesUiAction
    data class OnChequeFilterSelected(val filter: ChequeStatus?): SalesChequesUiAction
    data class OnTransferCheque(val cheque: ChequePayment, val isSupplier: Boolean): SalesChequesUiAction
}