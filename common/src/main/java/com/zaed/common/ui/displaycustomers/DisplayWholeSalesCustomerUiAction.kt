package com.zaed.common.ui.displaycustomers

import com.zaed.common.data.model.customer.WholeSaleCustomer

sealed interface DisplayWholeSalesCustomerUiAction {
    data object OnBackClicked : DisplayWholeSalesCustomerUiAction
    data object OnShowNavDrawer : DisplayWholeSalesCustomerUiAction
    data object OnAddGoldWholeSaleCustomerClicked : DisplayWholeSalesCustomerUiAction
    data object OnAddSilverWholeSaleCustomerClicked : DisplayWholeSalesCustomerUiAction
    data class OnSearchQueryChanged(val query: String) : DisplayWholeSalesCustomerUiAction
    data class OnCustomerClicked(val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction

    data class OnCustomerDeleted(val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction

    data class OnEditGoldCustomerClicked(val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction

    data class OnEditSilverCustomerClicked(val customer: WholeSaleCustomer) :
        DisplayWholeSalesCustomerUiAction
}