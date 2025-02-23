package com.zaed.distributor.ui.customerdetails

import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentType

sealed interface CustomerDetailsUiAction {

    data object OnBackClicked : CustomerDetailsUiAction
    data object OnSaveClicked : CustomerDetailsUiAction
    data object OnEditCustomer : CustomerDetailsUiAction
    data class OnAmountChanged(val amount: Double) : CustomerDetailsUiAction
    data class OnTypeChanged(val type: PaymentType) : CustomerDetailsUiAction
    data class OnChangeValueDirection(val isGiven: Boolean) : CustomerDetailsUiAction
    data class DeletePayment(val moneyPayment: MoneyPayment) : CustomerDetailsUiAction
    data class EditPayment(val moneyPayment: MoneyPayment) : CustomerDetailsUiAction
    data object OnConfirmEditPayment : CustomerDetailsUiAction
    data class OnDeleteProductSale(val saleId: String): CustomerDetailsUiAction
    data class OnDeleteGoldSale(val saleId: String): CustomerDetailsUiAction
    data class OnEditProductSale(val saleId: String): CustomerDetailsUiAction
    data class OnEditGoldSale(val saleId: String): CustomerDetailsUiAction
    data class OnProductSaleClicked(val saleId: String): CustomerDetailsUiAction
    data class OnGoldSaleClicked(val saleId: String): CustomerDetailsUiAction


}