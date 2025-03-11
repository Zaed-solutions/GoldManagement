package com.zaed.manager.ui.salescheques

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
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomerSalesUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import com.zaed.common.domain.payment.FetchCustomerPaymentsUseCase
import com.zaed.common.domain.sale.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.DeleteWholesaleProductSaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesChequesScreenViewModel(
    private val fetchCustomerPaymentsUseCase: FetchCustomerPaymentsUseCase,
    private val fetchWholeSalesCustomerSalesUseCase: FetchWholesaleCustomerSalesUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val getWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val deleteProductSaleUseCase: DeleteWholesaleProductSaleUseCase,
    private val deleteGoldSaleUseCase: DeleteWholesaleGoldSaleUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase
    ) : ViewModel() {
        val TAG = "CustomerDetailsViewModel"

    private val _uiState = MutableStateFlow(SalesChequesUiState())
    val uiState = _uiState.asStateFlow()

    fun init(customerId: String) {
        getCustomerDetails(customerId)
        _uiState.update {
            it.copy(customer = it.customer.copy(id = customerId))
        }
        getCustomerPayments(customerId)
        getCustomerTransactions(customerId)
        getCurrentUser()
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

    fun handleUiAction(action: SalesChequesUiAction) {
        when (action) {
            is SalesChequesUiAction.DeletePayment -> deletePayment(action.cashPayment)
            is SalesChequesUiAction.OnDeleteProductSale -> deleteProductSale(action.saleId)
            is SalesChequesUiAction.OnDeleteGoldSale -> deleteGoldSale(action.saleId)
            else -> {}
        }
    }

    private fun deleteProductSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            deleteProductSaleUseCase(
                DeleteWholesaleProductSaleRequest(
                    saleId = saleId,
                    distributorId = uiState.value.currentDistributor.id,
                    distributorName = uiState.value.currentDistributor.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteProductSale: success")
            }.onFailure {
                Log.e(TAG, "deleteProductSale: ${it.message}",it )
                it.printStackTrace()
            }
        }
    }

    private fun deleteGoldSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            deleteGoldSaleUseCase(
                DeleteWholesaleGoldSaleRequest(
                    saleId = saleId,
                    distributorId = uiState.value.currentDistributor.id,
                    distributorName = uiState.value.currentDistributor.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteGoldSale: success")
            }.onFailure {
                Log.e(TAG, "deleteGoldSale: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

     fun confirmEditPayment(oldPayment: Payment, newPayment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            editPaymentUseCase(
                request = EditPaymentRequest(
                    customerId = uiState.value.customer.id,
                    oldPayment = oldPayment,
                    newPayment = newPayment
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

    fun addPayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            addPaymentUseCase(
                request = AddNewPaymentRequest(
                    customerId = uiState.value.customer.id,
                    payment = payment
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false,
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

//    private fun updateType(type: PaymentType) {
//        _uiState.update {
//            it.copy(
//                currentPayment = it.currentPayment.copy(
//                    type = type
//                )
//            )
//        }
//    }


//    private fun updateAmount(amount: Double) {
//        _uiState.update {
//            it.copy(
//                currentPayment = it.currentPayment.copy(
//                    amount = amount
//                )
//            )
//        }
//    }

    private fun getCustomerPayments(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            fetchCustomerPaymentsUseCase(
                request = FetchCustomerPaymentsRequest(customerId)
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update {oldState->
                        oldState.copy(
                            loading = false,
                            payments = data
                                .filter { it.receiptNumber.isEmpty() || it.type == PaymentType.FUTURES }
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

