package com.zaed.distributor.ui.customerdetails

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType

sealed interface CustomerDetailsUiAction {

    data object OnBackClicked : CustomerDetailsUiAction
    data object OnSaveClicked : CustomerDetailsUiAction
    data class OnAmountChanged(val amount: Double) : CustomerDetailsUiAction
    data class OnTypeChanged(val type: PaymentType) : CustomerDetailsUiAction
    data class OnChangeValueDirection(val isGiven: Boolean) : CustomerDetailsUiAction
    data class DeletePayment(val payment: Payment) : CustomerDetailsUiAction
    data class EditPayment(val payment: Payment) : CustomerDetailsUiAction

}