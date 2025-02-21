package com.zaed.distributor.ui.customerdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.domain.customer.FetchWholesaleCustomerSalesUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import com.zaed.common.domain.payment.FetchCustomerPaymentsUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class CustomerDetailsViewModel(
    private val fetchCustomerPaymentsUseCase: FetchCustomerPaymentsUseCase,
    private val fetchWholeSalesCustomerSalesUseCase: FetchWholesaleCustomerSalesUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val getWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(customerId: String) {
        getCustomerDetails(customerId)
        _uiState.update {
            it.copy(customer = it.customer.copy(id = customerId))
        }
        getCustomerPayments(customerId)
        getCustomerTransactions(customerId)
    }

    private fun getCustomerTransactions(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            fetchWholeSalesCustomerSalesUseCase(
                request = FetchWholesaleCustomerSalesRequest(
                    customerId = customerId
                )
            ).collect{result->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            sales = data
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
    }

    fun handleUiAction(action: CustomerDetailsUiAction) {
        when (action) {
            is CustomerDetailsUiAction.OnAmountChanged -> updateAmount(action.amount)
            is CustomerDetailsUiAction.OnChangeValueDirection -> updateAmountDirection(action.isGiven)
            is CustomerDetailsUiAction.DeletePayment -> deletePayment(action.payment)
            is CustomerDetailsUiAction.EditPayment -> updateCurrentPayment(action.payment)
            CustomerDetailsUiAction.OnConfirmEditPayment -> confirmEditPayment()
            CustomerDetailsUiAction.OnSaveClicked -> addPayment()
            is CustomerDetailsUiAction.OnTypeChanged -> updateType(action.type)
            else -> {}
        }
    }

    private fun confirmEditPayment() {
        Log.d("TAG", "confirmEditPayment: ${uiState.value.currentPayment}")
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            editPaymentUseCase(
                request = EditPaymentRequest(
                    customerId = uiState.value.customer.id,
                    newPayment = uiState.value.currentPayment.copy(
                        amount = if (uiState.value.paymentDirection) uiState.value.currentPayment.amount else uiState.value.currentPayment.amount.unaryMinus()
                    ),
                    oldAmount = uiState.value.tempPayment.amount
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
                getCustomerDetails(uiState.value.customer.id)
            }.onFailure {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }
        }
    }

    private fun updateCurrentPayment(payment: Payment) {
        _uiState.update {
            it.copy(
                tempPayment = payment,
                currentPayment = payment.copy(amount = payment.amount.absoluteValue),
                paymentDirection = payment.amount > 0
            )
        }
    }

    private fun deletePayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            deletePaymentUseCase.invoke(
                DeletePaymentRequest(
                    paymentId = payment.id,
                    customerId = uiState.value.customer.id,
                    amount = payment.amount
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
                getCustomerDetails(uiState.value.customer.id)
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

    private fun addPayment() {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value.currentPayment.amount == 0.0) return@launch
            _uiState.update {
                it.copy(loading = true)
            }
            addPaymentUseCase(
                request = AddNewPaymentRequest(
                    customerId = uiState.value.customer.id,
                    payment = uiState.value.currentPayment.copy(
                        amount = if (uiState.value.paymentDirection) uiState.value.currentPayment.amount else uiState.value.currentPayment.amount.unaryMinus()
                    )
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false,
                        currentPayment = Payment()
                    )
                }
                getCustomerDetails(uiState.value.customer.id)
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

    private fun updateType(type: PaymentType) {
        _uiState.update {
            it.copy(
                currentPayment = it.currentPayment.copy(
                    type = type
                )
            )
        }
    }

    private fun updateAmountDirection(value: Boolean) {
        Log.d("TAG", "updateAmountDirection: $value")
        _uiState.update {
            it.copy(
                paymentDirection = value,
            )
        }
    }

    private fun updateAmount(amount: Double) {
        _uiState.update {
            it.copy(
                currentPayment = it.currentPayment.copy(
                    amount = amount
                )
            )
        }
    }

    private fun getCustomerPayments(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            fetchCustomerPaymentsUseCase(
                request = FetchCustomerPaymentsRequest(customerId)
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            payments = data
                                .sortedByDescending { it.createdAt }
                                .groupBy { it.createdAt.format(DateFormat.DATE) }
                                .mapValues { (_, value) -> value.sortedByDescending { it.createdAt.time } }
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



    private fun getCustomerDetails(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            getWholeSalesCustomerUseCase(customerId).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        customer = data
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
}

