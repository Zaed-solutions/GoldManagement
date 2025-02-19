package com.zaed.distributor.ui.displaycustomers

import com.zaed.common.data.model.WholeSaleCustomer

sealed interface DisplayWholeSalesCustomerUiAction {
    data object OnBackClicked : DisplayWholeSalesCustomerUiAction
    data object OnAddWholeSaleCustomerClicked : DisplayWholeSalesCustomerUiAction
    data class OnSearchQueryChanged(val query: String) : DisplayWholeSalesCustomerUiAction
    data class OnCustomerClicked (val customer: WholeSaleCustomer) : DisplayWholeSalesCustomerUiAction
}