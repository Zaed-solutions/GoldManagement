package com.zaed.manager.ui.salescheques

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.cheque.FetchSalesChequesUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesChequesScreenViewModel(
    private val getSalesChequesUseCase: FetchSalesChequesUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val getWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase
    ) : ViewModel() {
        val TAG = "CustomerDetailsViewModel"

    private val _uiState = MutableStateFlow(SalesChequesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCustomerPayments()
        getCurrentUser()
    }
    private fun fetchCustomersSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCustomersByNameUseCase(
                FetchWholesaleCustomersByNameRequest(
                    name = uiState.value.customerSearchQuery,
                    distributorId = uiState.value.currentDistributor.id
                )
            ).onSuccess { data ->
                launch(Dispatchers.Main) {
                    _uiState.update { oldState ->
                        oldState.copy(suggestedCustomers = data)
                    }
                }
            }.onFailure { e ->
                Log.e(TAG, "fetchCustomersSuggestions: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    fun updateCustomer(customer: WholeSaleCustomer) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(selectedCustomer = customer)
            }
        }
    }
    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            currentDistributor = data
                        )
                    }
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    customerSearchQuery = query,
                )
            }
            fetchCustomersSuggestions()
        }
    }
    fun handleUiAction(action: SalesChequesUiAction) {
        when (action) {
            is SalesChequesUiAction.DeletePayment -> deletePayment(action.cashPayment)
            else -> {}
        }
    }


     fun confirmEditPayment(oldPayment: Payment, newPayment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            editPaymentUseCase(
                request = EditPaymentRequest(
                    customerId = uiState.value.selectedCustomer.id,
                    oldPayment = oldPayment,
                    newPayment = newPayment
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }
        }
    }

    private fun deletePayment(cashPayment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            deletePaymentUseCase.invoke(
                DeletePaymentRequest(
                    payment = cashPayment,
                    employeeId = uiState.value.currentDistributor.id,
                    employeeName = uiState.value.currentDistributor.fullName,
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }.onFailure {
                it.printStackTrace()
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }

        }
    }

    fun addPayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            addPaymentUseCase(
                request = AddNewPaymentRequest(
                    customerId = uiState.value.selectedCustomer.id,
                    payment = payment
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false,
                    )
                }
            }.onFailure {
                it.printStackTrace()
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }


        }
    }

    private fun getCustomerPayments() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            getSalesChequesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {oldState->
                        oldState.copy(
                            loading = false,
                            payments = data
                                .sortedByDescending { it.createdAt }
                        )
                    }
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                    )
                }
            }
        }
    }
}

