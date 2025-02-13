package com.zaed.cashier.ui.addsale

sealed interface AddSaleUiAction {
    data object OnBackClicked : AddSaleUiAction
    data object OnAddClicked : AddSaleUiAction
    data class OnUpdateCustomerName(val customerName: String) : AddSaleUiAction
    data class OnUpdateCustomerPhone(val customerPhoneNumber: String) : AddSaleUiAction
    data class OnUpdateCustomerEmail(val customerEmail: String) : AddSaleUiAction
}