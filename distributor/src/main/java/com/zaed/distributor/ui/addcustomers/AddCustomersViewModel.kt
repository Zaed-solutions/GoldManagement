package com.zaed.distributor.ui.addcustomers

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.Zone
import com.zaed.common.data.model.customer.request.EditWholeSalesCustomerRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.AddWholeSaleCustomerUseCase
import com.zaed.common.domain.customer.EditWholeSalesCustomerUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.ui.auth.FieldsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCustomersViewModel(
    private val addWholeSaleCustomerUseCase: AddWholeSaleCustomerUseCase,
    private val getWholeSaleCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val editWholeSaleCustomerUseCase: EditWholeSalesCustomerUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddCustomersState())
    val state = _state.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _state.update {
                        it.copy(
                            distributor = data,
                            request = it.request.copy(
                                distributorId = data.id
                            )
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

        fun handleAction(action: AddCustomersUiAction) {
            when (action) {
                AddCustomersUiAction.OnSave -> {
                    saveCustomer()
                }

                is AddCustomersUiAction.UpdateAddress -> _state.update {
                    it.copy(
                        request = it.request.copy(
                            address = action.address
                        )
                    )
                }

                is AddCustomersUiAction.UpdateCity -> _state.update {
                    it.copy(
                        request = it.request.copy(
                            city = action.city
                        )
                    )
                }

                is AddCustomersUiAction.UpdateEmail -> _state.update {
                    it.copy(
                        request = it.request.copy(
                            email = action.email
                        )
                    )
                }

                is AddCustomersUiAction.UpdateName -> _state.update {
                    it.copy(
                        fieldsError = FieldsError.NONE,
                        request = it.request.copy(name = action.name)
                    )
                }

                is AddCustomersUiAction.UpdatePhone -> _state.update {
                    it.copy(
                        request = it.request.copy(
                            phone = action.phone
                        )
                    )
                }

                is AddCustomersUiAction.UpdateZone -> _state.update {
                    it.copy(
                        request = it.request.copy(
                            zone = action.zone
                        )
                    )
                }

                is AddCustomersUiAction.OnEdit -> {
                    editCustomer()
                }

                else -> {}
            }
        }

        private fun editCustomer() {
            viewModelScope.launch(Dispatchers.IO) {
                editWholeSaleCustomerUseCase(
                    EditWholeSalesCustomerRequest(
                        id = state.value.customerId,
                        updatedData = state.value.request
                    )
                ).onSuccess {
                    _state.update {
                        it.copy(
                            successStatus = true
                        )
                    }
                }
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
            when {
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

        fun getCustomer(customerId: String) {
            viewModelScope.launch(Dispatchers.IO) {
                getWholeSaleCustomerUseCase(customerId).onSuccess { data ->
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            customerId = data.id,
                            request = AddWholeSaleCustomerRequest(
                                name = data.name,
                                email = data.email,
                                phone = data.phone,
                                address = data.address,
                                city = data.city,
                                zone = data.zone
                            )
                        )
                    }
                }
            }
        }
    }

    sealed interface AddCustomersUiAction {
        data object OnBack : AddCustomersUiAction
        data object OnSave : AddCustomersUiAction
        data object OnEdit : AddCustomersUiAction
        data class UpdateName(val name: String) : AddCustomersUiAction
        data class UpdateEmail(val email: String) : AddCustomersUiAction
        data class UpdatePhone(val phone: String) : AddCustomersUiAction
        data class UpdateAddress(val address: String) : AddCustomersUiAction
        data class UpdateCity(val city: String) : AddCustomersUiAction
        data class UpdateZone(val zone: Zone) : AddCustomersUiAction
    }

    data class AddCustomersState(
        val isLoading: Boolean = false,
        val distributor :User = User(),
        val error: AddCustomersScreenError? = null,
        val fieldsError: FieldsError = FieldsError.NONE,
        val successStatus: Boolean = false,
        val isEditMode: Boolean = false,
        val customerId: String = "",
        val request: AddWholeSaleCustomerRequest = AddWholeSaleCustomerRequest()
    )

    enum class AddCustomersScreenError(@StringRes val message: Int) {
        NO_INTERNET(com.zaed.common.R.string.no_internet_connection),
        UNKNOWN(com.zaed.common.R.string.unknown_error)
    }
