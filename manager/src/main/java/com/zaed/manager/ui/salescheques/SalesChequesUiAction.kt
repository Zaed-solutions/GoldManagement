package com.zaed.manager.ui.salescheques

import com.zaed.common.data.model.payment.Payment

sealed interface SalesChequesUiAction {

    data object OnBackClicked : SalesChequesUiAction
    data object OnEditCustomer : SalesChequesUiAction
    data class DeletePayment(val cashPayment: Payment) : SalesChequesUiAction


}