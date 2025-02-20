package com.zaed.distributor.ui.customerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.sale.WholesaleSale
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.FetchCustomerPaymentsUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerDetailsViewModel(
    private val fetchCustomerPaymentsUseCase: FetchCustomerPaymentsUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val getWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(customerId: String) {
        getCustomerDetails(customerId)
        _uiState.update {
            it.copy(customer = it.customer.copy(id = customerId))
        }
    }
    fun handleUiAction(action: CustomerDetailsUiAction) {
        when (action) {
            is CustomerDetailsUiAction.OnAmountChanged -> updateAmount(action.amount)
            is CustomerDetailsUiAction.OnChangeValueDirection -> updateAmountDirection(action.isGiven)
            CustomerDetailsUiAction.OnDeletePaymentClicked -> TODO()
            CustomerDetailsUiAction.OnEditPaymentClicked -> TODO()
            CustomerDetailsUiAction.OnSaveClicked -> addPayment()
            is CustomerDetailsUiAction.OnTypeChanged -> updateType(action.type)
            else -> {}
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
                    payment = uiState.value.currentPayment
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

    private fun updateAmountDirection(given: Boolean) {
        _uiState.update {
            it.copy(
                currentPayment = it.currentPayment.copy(
                    amount = it.currentPayment.amount * -1
                )
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

    private fun getCustomerTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            fetchCustomerPaymentsUseCase(
                /*todo*/
                request = FetchCustomerPaymentsRequest(uiState.value.customer.id)
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
                getCustomerTransactions()
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
    data class CustomerDetailsUiState(
        val loading: Boolean = false,
        val customer: WholeSaleCustomer = WholeSaleCustomer(),
        val payments: Map<String, List<Payment>> = emptyMap(),
        val sales : List<WholesaleSale> = emptyList(),
        val currentPayment: Payment = Payment()
    )

