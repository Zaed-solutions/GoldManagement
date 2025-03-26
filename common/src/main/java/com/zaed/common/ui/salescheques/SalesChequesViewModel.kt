package com.zaed.common.ui.salescheques

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.cheque.FetchManagerChequesUseCase
import com.zaed.common.domain.cheque.FetchSalesChequesUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import com.zaed.common.domain.supplier.AddSupplierUseCase
import com.zaed.common.domain.supplier.DeleteSupplierUseCase
import com.zaed.common.domain.supplier.FetchSuppliersUseCase
import com.zaed.common.domain.supplier.UpdateSupplierUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesChequesScreenViewModel(
    private val getSalesChequesUseCase: FetchSalesChequesUseCase,
    private val getManagerChequesUseCase: FetchManagerChequesUseCase,
    private val addPaymentUseCase: AddNewPaymentUseCase,
    private val fetchSuppliersUseCase: FetchSuppliersUseCase,
    private val addSupplierUseCase: AddSupplierUseCase,
    private val updateSupplierUseCase: UpdateSupplierUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase
    ) : ViewModel() {
        val TAG = "CustomerDetailsViewModel"

    private val _uiState = MutableStateFlow(SalesChequesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCustomersPayments()
        getSuppliersPayments()
        getCurrentUser()
        fetchSuppliers()
    }
    private fun fetchCustomersSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCustomersByNameUseCase(
                FetchWholesaleCustomersByNameRequest(
                    name = uiState.value.customerSearchQuery,
                    distributorId = uiState.value.currentUser.let{ if(it.role == UserRole.ACCOUNTANT) "" else it.id }
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
    private fun fetchSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSuppliersUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSuppliers = data,
                            filteredSuppliers = data
                        )
                    }
                    filterSuppliers()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSuppliers: ${e.message}", e)
                }
            }
        }
    }

    private fun filterSuppliers() {
        viewModelScope.launch(Dispatchers.Default) {
            val filteredSuppliers = uiState.value.allSuppliers.filter { supplier ->
                listOf(supplier.name, supplier.phone).any {
                    it.contains(uiState.value.searchQuery, ignoreCase = true)
                }
            }
            _uiState.update { oldState ->
                oldState.copy(
                    loading = false,
                    filteredSuppliers = filteredSuppliers
                )
            }
        }
    }

    private fun updateSupplierSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    loading = true,
                    searchQuery = query
                )
            }
            filterSuppliers()
        }
    }

    private fun deleteSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            deleteSupplierUseCase(
                DeleteSupplierRequest(
                    supplierId = supplier.id
                )
            ).onSuccess {
                Log.d(TAG, "deleteSupplier: success")
            }.onFailure {
                Log.e(TAG, "deleteSupplier: ${it.message}",it )
            }
        }
    }

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            updateSupplierUseCase(
                UpdateSupplierRequest(supplier)
            ).onSuccess {
                Log.d(TAG, "updateSupplier: success")
            }.onFailure {
                Log.e(TAG, "updateSupplier: ${it.message}", it)
            }
        }
    }

    fun addSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            addSupplierUseCase(
                AddSupplierRequest(
                    supplier = supplier
                )
            ).onSuccess {
                Log.d(TAG, "addSupplier: success")
            }.onFailure {
                Log.e(TAG, "addSupplier: ${it.message}", it)
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            currentUser = data
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
            is SalesChequesUiAction.OnAddPayment -> addPayment(action.payment, action.isSupplier)
            is SalesChequesUiAction.OnEditPayment -> editPayment(action.oldPayment, action.newPayment, isSupplier = action.isSupplier)
            is SalesChequesUiAction.OnCustomerSearchQueryChanged -> updateSearchQuery(action.query)
            is SalesChequesUiAction.DeletePayment -> deletePayment(action.cashPayment)
            is SalesChequesUiAction.OnUpdateSupplierSearchQuery -> updateSupplierSearchQuery(action.query)
            is SalesChequesUiAction.OnAddSupplier -> addSupplier(action.supplier)
            is SalesChequesUiAction.OnUpdateChequeSearchQuery -> updateChequeSearchQuery(action.query)
            is SalesChequesUiAction.OnChequeFilterSelected -> updateChequeFilter(action.filter)
            is SalesChequesUiAction.OnAccountSelected -> updateSelectedAccount(action.account)
            is SalesChequesUiAction.OnTransferCheque -> transferCheque(action.cheque, action.isSupplier)
            else -> {}
        }
    }

    private fun transferCheque(cheque: ChequePayment, isSupplier: Boolean) {
        viewModelScope.launch {
            val account = uiState.value.selectedAccount
            val newCheque = cheque.copy(
                receiverName = account.name,
                receiverId = account.id,
                chequeStatus = ChequeStatus.TRANSFERRED,
            )
            editPayment(
                oldPayment = cheque,
                newPayment = newCheque,
                isSupplier = isSupplier
            )
        }
    }

    private fun updateSelectedAccount(account: Account) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    selectedAccount = account
                )
            }
        }
    }

    private fun updateChequeFilter(filter: ChequeStatus?) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    selectedChequeFilter = filter
                )
            }
            filterCheques()
        }
    }

    private fun filterCheques() {
        viewModelScope.launch (Dispatchers.Default){
            val filter = uiState.value.selectedChequeFilter
            val query = uiState.value.chequeSearchQuery
            if(query.isBlank()){
                _uiState.update { oldState ->
                    oldState.copy(
                        filteredSalesCheques = oldState.allSalesCheques,
                        filteredManagerCheques = oldState.allManagerCheques,
                        loading = false
                    )
                }
            } else {
                val filteredSalesCheques = uiState.value.allSalesCheques.filter { cheque ->
                    listOf(cheque.senderName, cheque.receiverName).any{ it.contains(query, ignoreCase = true) }
                }
                val filteredManagerCheques = uiState.value.allManagerCheques.filter { cheque ->
                    cheque.receiverName.contains(query, ignoreCase = true)
                }
                _uiState.update { oldState ->
                    oldState.copy(
                        filteredSalesCheques = filteredSalesCheques,
                        filteredManagerCheques = filteredManagerCheques,
                        loading = false
                    )
                }
            }
            if(filter != null){
                val filteredSalesCheques = uiState.value.filteredSalesCheques.filter { cheque ->
                    cheque.chequeStatus == filter
                }
                val filteredManagerCheques = uiState.value.filteredManagerCheques.filter { cheque ->
                    cheque.chequeStatus == filter
                }
                _uiState.update { oldState ->
                    oldState.copy(
                        filteredSalesCheques = filteredSalesCheques,
                        filteredManagerCheques = filteredManagerCheques,
                        loading = false
                    )
                }
            }
        }
    }

    private fun updateChequeSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    chequeSearchQuery = query
                )
            }
            filterCheques()
        }
    }


    private fun editPayment(oldPayment: Payment, newPayment: Payment, isSupplier: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            editPaymentUseCase(
                request = EditPaymentRequest(
                    isSupplier = isSupplier,
                    accountId = uiState.value.selectedAccount.id,
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
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName,
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

    fun addPayment(payment: Payment, isSupplier: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            addPaymentUseCase(
                request = AddNewPaymentRequest(
                    isSupplier = isSupplier,
                    accountId = uiState.value.selectedAccount.id,
                    payment = payment
                )
            ).onSuccess {
                Log.d(TAG, "addPayment: success")
                _uiState.update {
                    it.copy(
                        loading = false,
                    )
                }
            }.onFailure {
                Log.e(TAG, "addPayment: ${it.message}", it)
                _uiState.update {
                    it.copy(
                        loading = false
                    )
                }
            }
        }
    }

    private fun getCustomersPayments() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            getSalesChequesUseCase().collect { result ->
                result.onSuccess { data ->
                    Log.d(TAG, "getCustomersPayments: ${data}")
                    _uiState.update {oldState->
                        oldState.copy(
                            loading = false,
                            allSalesCheques = data.sortedByDescending { it.createdAt },
                            uncashedSalesCheques = data.filter { it.chequeStatus == ChequeStatus.RECEIVED }
                        )
                    }
                    filterCheques()
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                    )
                }
            }
        }
    }
    private fun getSuppliersPayments() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            getManagerChequesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {oldState->
                        oldState.copy(
                            loading = false,
                            allManagerCheques = data
                                .sortedByDescending { it.createdAt }
                        )
                    }
                    filterCheques()
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                    )
                }
            }
        }
    }
}