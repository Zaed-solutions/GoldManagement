package com.zaed.common.ui.displaycustomers

import com.zaed.common.data.model.customer.WholeSaleCustomer

sealed interface DisplayWholeSalesCustomerUiAction {
    data object OnBackClicked : DisplayWholeSalesCustomerUiAction
    data object OnShowNavDrawer : DisplayWholeSalesCustomerUiAction
    data object OnAddWholeSaleCustomerClicked : DisplayWholeSalesCustomerUiAction
    data class OnSearchQueryChanged(val query: String) : DisplayWholeSalesCustomerUiAction
    data class OnCustomerClicked (val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction
    data class OnCustomerDeleted( val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction
    data class OnEditCustomerClicked( val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction
}