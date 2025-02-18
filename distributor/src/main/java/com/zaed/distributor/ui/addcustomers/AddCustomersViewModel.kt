package com.zaed.distributor.ui.addcustomers

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.Zone
import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import com.zaed.common.domain.AddWholeSaleCustomerUseCase
import com.zaed.common.ui.auth.FieldsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCustomersViewModel(
    private val addWholeSaleCustomerUseCase: AddWholeSaleCustomerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddCustomersState())
    val state = _state.asStateFlow()
    fun handleAction(action: AddCustomersUiAction) {
        when (action) {
            AddCustomersUiAction.OnSave -> {
                saveCustomer()
            }
            is AddCustomersUiAction.UpdateAddress -> _state.update { it.copy(request = it.request.copy(address = action.address)) }
            is AddCustomersUiAction.UpdateCity -> _state.update { it.copy(request = it.request.copy(city = action.city)) }
            is AddCustomersUiAction.UpdateEmail -> _state.update { it.copy(request = it.request.copy(email = action.email)) }
            is AddCustomersUiAction.UpdateName -> _state.update { it.copy(fieldsError = FieldsError.NONE, request = it.request.copy(name = action.name)) }
            is AddCustomersUiAction.UpdatePhone -> _state.update { it.copy(request = it.request.copy(phone = action.phone)) }
            is AddCustomersUiAction.UpdateZone -> _state.update { it.copy(request = it.request.copy(zone = action.zone)) }
            else -> {}
        }
    }

    private fun saveCustomer() {
        if (validRequest()) {
            viewModelScope.launch(Dispatchers.IO) {
                addWholeSaleCustomerUseCase(state.value.request).onSuccess {
                    _state.update {
                        it.copy(
                            successStatus = true
                        )
                    }
                }.onFailure {
                    _state.update {
                        it.copy(
                            error = AddCustomersScreenError.UNKNOWN
                        )
                    }
                }
            }
        }
    }

    private fun validRequest(): Boolean {
        when{
            state.value.request.name.isEmpty() -> {
                _state.update {
                    it.copy(
                        fieldsError = FieldsError.EMPTY_FULL_NAME
                    )
                }
                return false
            }
            else -> {
                return true
            }
        }
    }
}

sealed interface AddCustomersUiAction {
    data object OnBack : AddCustomersUiAction
    data object OnSave : AddCustomersUiAction
    data class UpdateName(val name: String) : AddCustomersUiAction
    data class UpdateEmail(val email: String) : AddCustomersUiAction
    data class UpdatePhone(val phone: String) : AddCustomersUiAction
    data class UpdateAddress(val address: String) : AddCustomersUiAction
    data class UpdateCity(val city: String) : AddCustomersUiAction
    data class UpdateZone(val zone: Zone) : AddCustomersUiAction
}

data class AddCustomersState (
    val isLoading: Boolean = false,
    val error: AddCustomersScreenError? = null,
    val fieldsError: FieldsError = FieldsError.NONE,
    val successStatus: Boolean = false,
    val request :AddWholeSaleCustomerRequest = AddWholeSaleCustomerRequest()
)
enum class AddCustomersScreenError(@StringRes val message: Int) {
    NO_INTERNET(com.zaed.common.R.string.no_internet_connection),
    UNKNOWN(com.zaed.common.R.string.unknown_error)
}
