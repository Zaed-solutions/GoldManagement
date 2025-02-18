package com.zaed.distributor.ui.displaycustomers

sealed interface DisplayWholeSalesCustomerUiAction {
    data object OnBackClicked : DisplayWholeSalesCustomerUiAction
    data object OnAddWholeSaleCustomerClicked : DisplayWholeSalesCustomerUiAction
    data class OnSearchQueryChanged(val query: String) : DisplayWholeSalesCustomerUiAction
}