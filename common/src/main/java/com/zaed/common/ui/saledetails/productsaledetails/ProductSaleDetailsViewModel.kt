package com.zaed.common.ui.saledetails.productsaledetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.DeleteWholesaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductSaleDetailsViewModel(
    private val fetchSaleUseCase: FetchWholesaleUseCase,
    private val fetchSalePaymentsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val getCurrentUseUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteWholesaleUseCase: DeleteWholesaleUseCase,
    private val updateWholesaleUseCase: UpdateWholesaleUseCase,
    private val fetchCustomerUseCase: GetWholeSalesCustomerUseCase
): ViewModel() {
    private val TAG = "ProductSaleDetailsViewModel"
    private val _uiState = MutableStateFlow(ProductSaleDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(saleId: String){
        fetchSale(saleId)
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUseUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSaleUseCase(
                FetchWholesaleRequest(saleId)
            ).onSuccess { data ->
                _uiState.update { oldState->
                    oldState.copy(sale = data)
                }
                fetchPayments(data.paymentsIds)
                fetchCustomer(data.customerId)
            }.onFailure {
                Log.e(TAG, "fetchSale: ${it.message}", it)
            }
        }
    }
    private fun fetchCustomer(customerId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchCustomerUseCase(customerId).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(customer = data)
                }
            }
        }
    }

    private fun fetchPayments(paymentsIds: List<String>) {
        viewModelScope.launch (Dispatchers.IO){
            fetchSalePaymentsUseCase(
                FetchPaymentsByIdsRequest(paymentsIds)
            ).onSuccess { data ->
                _uiState.update { oldState->
                    oldState.copy(cashPayments = data)
                }
            }.onFailure {
                Log.e(TAG, "fetchPayments: ${it.message}",it )
            }
        }
    }


    fun handleAction(action: SaleDetailsUiAction){
        when(action){
            is SaleDetailsUiAction.OnDeleteSale -> deleteSale()
            SaleDetailsUiAction.OnRequestReceipt -> requestReceipt()
            else -> Unit
        }
    }

    private fun requestReceipt() {
        viewModelScope.launch (Dispatchers.IO){
            val sale = uiState.value.sale.copy(receiptStatus = ReceiptStatus.PENDING)
            val logs = sale.logs.toMutableList().apply {
                add(
                    ChangeLog(
                        employeeName = uiState.value.currentUser.fullName,
                        employeeId = uiState.value.currentUser.id,
                        type = LogType.UPDATE
                    )
                )
            }
            updateWholesaleUseCase(
                UpdateWholesaleRequest(
                    sale = sale.copy(logs = logs),
                    payments = uiState.value.cashPayments,
                    employeeName = uiState.value.currentUser.fullName,
                    employeeId = uiState.value.currentUser.id
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(sale = sale)
                }
            }.onFailure {
                Log.e(TAG, "requestReceipt: ${it.message}", it)
            }
        }
    }

    private fun deleteSale() {
        viewModelScope.launch (Dispatchers.IO){
            deleteWholesaleUseCase(
                DeleteWholesaleRequest(
                    saleId = uiState.value.sale.id
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(isSaleDeleted = true)
                }
            }.onFailure {
                Log.e(TAG, "deleteSale: ${it.message}", it)
            }
        }
    }
}