package com.zaed.manager.ui.salescheques

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType

sealed interface SalesChequesUiAction {

    data object OnBackClicked : SalesChequesUiAction
    data object OnSaveClicked : SalesChequesUiAction
    data object OnEditCustomer : SalesChequesUiAction
    data class OnAmountChanged(val amount: Double) : SalesChequesUiAction
    data class OnTypeChanged(val type: PaymentType) : SalesChequesUiAction
    data class OnChangeValueDirection(val isGiven: Boolean) : SalesChequesUiAction
    data class DeletePayment(val cashPayment: Payment) : SalesChequesUiAction
    data class EditPayment(val cashPayment: Payment) : SalesChequesUiAction
    data object OnConfirmEditPayment : SalesChequesUiAction
    data class OnDeleteProductSale(val saleId: String): SalesChequesUiAction
    data class OnDeleteGoldSale(val saleId: String): SalesChequesUiAction
    data class OnEditProductSale(val saleId: String): SalesChequesUiAction
    data class OnEditGoldSale(val saleId: String): SalesChequesUiAction
    data class OnProductSaleClicked(val saleId: String): SalesChequesUiAction
    data class OnGoldSaleClicked(val saleId: String): SalesChequesUiAction


}