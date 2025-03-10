package com.zaed.common.ui.saledetails.goldsaledetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleGoldSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.FetchGoldPaymentsByIdsUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleGoldSaleUseCase
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoldSaleDetailsViewModel(
    private val fetchSaleUseCase: FetchWholesaleGoldSaleUseCase,
    private val fetchMoneyPaymentsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val fetchGoldPaymentsUseCase: FetchGoldPaymentsByIdsUseCase,
    private val getCurrentUseUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteWholesaleGoldSaleUseCase: DeleteWholesaleGoldSaleUseCase,
    private val updateWholesaleGoldSaleUseCase: UpdateWholesaleGoldSaleUseCase,
    private val fetchCustomerUseCase: GetWholeSalesCustomerUseCase
): ViewModel() {
    private val TAG = "ProductSaleDetailsViewModel"
    private val _uiState = MutableStateFlow(GoldSaleDetailsUiState())
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
                FetchWholesaleGoldSaleRequest(saleId)
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

    private fun fetchPayments(
        moneyPaymentsIds: List<String>,
    ) {
        viewModelScope.launch (Dispatchers.IO){
            fetchMoneyPaymentsUseCase(
                FetchPaymentsByIdsRequest(moneyPaymentsIds)
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(payments = oldState.payments + data)
                }
                Log.d(TAG, "fetchPayments: $data")
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
            updateWholesaleGoldSaleUseCase(
                UpdateWholesaleGoldSaleRequest(
                    sale = sale.copy(logs = logs),
                    payments = uiState.value.payments,
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
            deleteWholesaleGoldSaleUseCase(
                DeleteWholesaleGoldSaleRequest(
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