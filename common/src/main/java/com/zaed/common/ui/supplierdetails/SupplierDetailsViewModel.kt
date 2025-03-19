package com.zaed.common.ui.supplierdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchSupplierPaymentsRequest
import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.cheque.FetchAllUnCashedSalesChequeUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import com.zaed.common.domain.payment.FetchSupplierPaymentsUseCase
import com.zaed.common.domain.purchase.FetchSupplierPurchasesUseCase
import com.zaed.common.domain.purchase.UpdatePurchaseUseCase
import com.zaed.common.domain.supplier.FetchSupplierUseCase
import com.zaed.common.domain.supplier.UpdateSupplierUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupplierDetailsViewModel(
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val fetchSupplierPaymentsUseCase: FetchSupplierPaymentsUseCase,
    private val fetchSupplierUseCase: FetchSupplierUseCase,
    private val updateSupplierUseCase: UpdateSupplierUseCase,
    private val fetchPurchasesUseCase: FetchSupplierPurchasesUseCase,
    private val updatePurchaseUseCase: UpdatePurchaseUseCase,
    private val fetchAllUnCashedSalesChequesUseCase: FetchAllUnCashedSalesChequeUseCase
) : ViewModel() {
    private val TAG: String = "SupplierDetailsViewModel"
    private val _uiState = MutableStateFlow(SupplierDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(supplierId: String) {
        fetchCurrentUser()
        fetchSupplier(supplierId)
        fetchPayments(supplierId)
        fetchPurchases(supplierId)
        fetchSalesCheques()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            currentUser = data,
                        )
                    }
                }
            }
        }
    }

    private fun fetchPurchases(supplierId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchPurchasesUseCase(
                FetchSupplierPurchasesRequest(
                    isManager = uiState.value.currentUser.role == UserRole.MANAGER,
                    supplierId = supplierId
                )
            ).collect{ result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(allPurchases = data)
                    }
                    filterPurchases()
                }.onFailure { e->
                    Log.e(TAG, "fetchPurchases: ${e.message}", e)
                }
            }
        }
    }

    private fun filterPurchases() {
        viewModelScope.launch (Dispatchers.Default){
            val query = uiState.value.purchasesSearchQuery
            val filteredPurchases = uiState.value.allPurchases.filter {
                it.receiptNumber.contains(query)
            }
            _uiState.update {
                it.copy(
                    filteredPurchases = filteredPurchases
                )
            }
        }
    }

    private fun fetchSalesCheques() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllUnCashedSalesChequesUseCase().onSuccess {
                _uiState.update { oldState ->
                    oldState.copy(
                        salesCheques = it
                    )
                }
            }.onFailure {
                Log.e(TAG, "fetchSalesCheques: ${it.message}", it)
            }
        }
    }

    private fun fetchPayments(supplierId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchSupplierPaymentsUseCase(
                FetchSupplierPaymentsRequest(
                    supplierId = supplierId,
                    isManager = uiState.value.currentUser.role == UserRole.MANAGER
                )
            ).collect{ result ->
                result.onSuccess { data->
                    _uiState.update {
                        it.copy(
                            payments = data
                        )
                    }
                }.onFailure {
                    Log.e(TAG, "fetchPurchases: ${it.message}", it)
                }
            }
        }
    }

    private fun fetchSupplier(supplierId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchSupplierUseCase(
                FetchSupplierRequest(
                    supplierId = supplierId
                )
            ).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        supplier = data
                    )
                }
            }.onFailure {
                Log.e(TAG, "fetchSupplier: ${it.message}", it)
            }
        }
    }

    fun handleAction(action: SupplierDetailsUiAction) {
        when (action) {
            is SupplierDetailsUiAction.AddPayment -> addPayment(action.payment)
            is SupplierDetailsUiAction.DeletePayment -> deletePayment(action.payment)
            is SupplierDetailsUiAction.UpdatePayment -> updatePayment(
                action.oldPayment,
                action.newPayment
            )
            is SupplierDetailsUiAction.UpdateSupplier -> updateSupplier(
                action.supplier
            )
            is SupplierDetailsUiAction.DeletePurchase -> deletePurchase(action.purchaseId)
            is SupplierDetailsUiAction.UpdatePurchasesSearchQuery -> updatePurchasesSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun updatePurchasesSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    purchasesSearchQuery = query
                )
            }
            filterPurchases()
        }
    }

    private fun deletePurchase(purchaseId: String) {
        viewModelScope.launch (Dispatchers.IO){
            val purchase = uiState.value.allPurchases.find { it.id == purchaseId }?: return@launch
            val currentEmployee = uiState.value.currentUser
            updatePurchaseUseCase(
                UpdatePurchaseRequest(
                    purchase = purchase.copy(
                        deleted = true,
                        logs = purchase.logs + ChangeLog(employeeName = currentEmployee.fullName, employeeId = currentEmployee.id, type = LogType.DELETE)
                    ),
                    payments = emptyList(),
                    employeeId = currentEmployee.id,
                    employeeName = currentEmployee.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deletePurchase: success")
            }.onFailure {
                Log.e(TAG, "deletePurchase: ${it.message}", it)
            }
        }
    }

    private fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            updateSupplierUseCase(
                UpdateSupplierRequest(supplier)
            ).onSuccess {
                Log.d(TAG, "updateSupplier: success")
                fetchSupplier(supplier.id)
            }.onFailure {
                Log.e(TAG, "updateSupplier: ${it.message}", it)
            }
        }
    }

    private fun updatePayment(oldPayment: Payment, newPayment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            editPaymentUseCase(
                request = EditPaymentRequest(
                    isSupplier = false,
                    customerId = uiState.value.supplier.id,
                    oldPayment = oldPayment,
                    newPayment = newPayment
                )
            ).onSuccess {
                Log.d(TAG, "updatePayment: success")
                fetchSupplier(uiState.value.supplier.id)
            }.onFailure {
                Log.e(TAG, "updatePayment: ${it.message}", it)
            }
        }
    }

    private fun deletePayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePaymentUseCase.invoke(
                DeletePaymentRequest(
                    isSupplier = true,
                    payment = payment,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName,
                )
            ).onSuccess {
                Log.d(TAG, "deletePayment: ")
                fetchSupplier(uiState.value.supplier.id)
            }.onFailure {
                Log.e(TAG, "deletePayment: ${it.message}", it)
            }

        }
    }

    private fun addPayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            addPaymentUseCase(
                request = AddNewPaymentRequest(
                    isSupplier = true,
                    customerId = uiState.value.supplier.id,
                    payment = payment.apply { this.customerId = uiState.value.supplier.id }
                )
            ).onSuccess {
                Log.d(TAG, "addPayment: success")
                fetchSupplier(uiState.value.supplier.id)
            }.onFailure {
                Log.e(TAG, "addPayment: ${it.message}", it)
            }
        }
    }
}