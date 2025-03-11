package com.zaed.common.ui.addcustomers

sealed interface AddCustomersUiAction {
    data object OnBack : AddCustomersUiAction
    data object OnSave : AddCustomersUiAction
    data object OnEdit : AddCustomersUiAction
    data class UpdateName(val name: String) : AddCustomersUiAction
    data class UpdateEmail(val email: String) : AddCustomersUiAction
    data class UpdatePhone(val phone: String) : AddCustomersUiAction
    data class UpdateAddress(val address: String) : AddCustomersUiAction
    data class UpdateCity(val city: String) : AddCustomersUiAction
}