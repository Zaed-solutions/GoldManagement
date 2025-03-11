package com.zaed.common.ui.addcustomers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
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
                            )
                        )
                    }
                }
            }
        }
    }

