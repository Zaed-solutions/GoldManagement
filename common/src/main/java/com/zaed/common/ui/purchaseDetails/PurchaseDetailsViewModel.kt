package com.zaed.common.ui.purchaseDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.purchase.FetchPurchaseUseCase
import com.zaed.common.domain.sale.DeleteWholesaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleUseCase
import com.zaed.common.domain.supplier.FetchSupplierUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchaseDetailsViewModel(
    private val fetchPurchaseUseCase: FetchPurchaseUseCase,
    private val fetchSalePaymentsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val getCurrentUseUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteWholesaleUseCase: DeleteWholesaleUseCase,
    private val updateWholesaleUseCase: UpdateWholesaleUseCase,
    private val fetchSupplierUseCase: FetchSupplierUseCase
): ViewModel() {
    private val TAG = "PurchaseDetailsViewModel"
    private val _uiState = MutableStateFlow(PurchaseDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(saleId: String){
        fetchPurchase(saleId)
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

    private fun fetchPurchase(purchaseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPurchaseUseCase(
                purchaseId
            ).onSuccess { data ->
                _uiState.update { oldState->
                    oldState.copy(purchase = data)
                }
                fetchPayments(data.paymentsIds)
                fetchSupplier(data.customerId)
            }.onFailure {
                Log.e(TAG, "fetchSale: ${it.message}", it)
            }
        }
    }
    private fun fetchSupplier(supplierId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchSupplierUseCase(
                request = FetchSupplierRequest(
                    supplierId = supplierId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(supplier = data)
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
            is SaleDetailsUiAction.OnDeleteSale -> {}
            SaleDetailsUiAction.OnRequestReceipt -> {}
            else -> Unit
        }
    }

//    private fun requestReceipt() {
//        viewModelScope.launch (Dispatchers.IO){
//            val sale = uiState.value.purchase.copy(receiptStatus = ReceiptStatus.PENDING)
//            val logs = sale.logs.toMutableList().apply {
//                add(
//                    ChangeLog(
//                        employeeName = uiState.value.currentUser.fullName,
//                        employeeId = uiState.value.currentUser.id,
//                        type = LogType.UPDATE
//                    )
//                )
//            }
//            updateWholesaleProductSaleUseCase(
//                UpdateWholesaleProductSaleRequest(
//                     = purchase.copy(logs = logs),
//                    payments = uiState.value.cashPayments,
//                    employeeName = uiState.value.currentUser.fullName,
//                    employeeId = uiState.value.currentUser.id
//                )
//            ).onSuccess {
//                _uiState.update {
//                    it.copy(sale = sale)
//                }
//            }.onFailure {
//                Log.e(TAG, "requestReceipt: ${it.message}", it)
//            }
//        }
//    }

//    private fun deletePurchase() {
//        viewModelScope.launch (Dispatchers.IO){
//            deleteWholesaleProductSaleUseCase(
//                DeleteWholesaleProductSaleRequest(
//                    saleId = uiState.value.sale.id
//                )
//            ).onSuccess {
//                _uiState.update {
//                    it.copy(isSaleDeleted = true)
//                }
//            }.onFailure {
//                Log.e(TAG, "deleteSale: ${it.message}", it)
//            }
//        }
//    }
}