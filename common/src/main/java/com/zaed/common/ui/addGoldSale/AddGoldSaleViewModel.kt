package com.zaed.common.ui.addGoldSale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.AddWholesaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddGoldSaleViewModel(
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val getCurrentWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase,
    private val fetchMoneyPaymentsByIdsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val fetchProductSaleUseCase: FetchWholesaleUseCase,
    private val addProductSaleUseCase: AddWholesaleUseCase,
    private val updateProductSaleUseCase: UpdateWholesaleUseCase
) : ViewModel() {
    private val TAG: String = "AddProductSaleVM"
    private val _uiState = MutableStateFlow(AddGoldSaleUiState())
    val uiState = _uiState.asStateFlow()
    fun init(saleId: String) {
        if (saleId.isNotBlank()) {
            fetchSale(saleId)
        }
        fetchCurrentUser()
    }

    private fun fetchSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchProductSaleUseCase(
                FetchWholesaleRequest(
                    saleId = saleId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        initialSale = data, sale = data
                    )
                }
                fetchCustomer(data.customerId)
                fetchPayments(data.paymentsIds)
            }.onFailure { e ->
                Log.e(TAG, "fetchSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun fetchPayments(paymentsIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMoneyPaymentsByIdsUseCase(
                FetchPaymentsByIdsRequest(
                    paymentsIds = paymentsIds
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(payments = data.filter { it.type != PaymentType.FUTURES })
                }
                updateTotalAmounts()
            }.onFailure { e ->
                Log.e(TAG, "fetchPayments: ${e.message}", e)
            }
        }
    }

    private fun fetchCustomer(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWholeSalesCustomerUseCase(customerId).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        selectedCustomer = data
                    )
                }
            }.onFailure {
                Log.e(TAG, "fetchCustomer: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }


    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
                Log.d(
                    "find the issue", "fetchCurrentUser: $result"
                )
            }
        }
    }

    fun handleAction(action: AddGoldSaleUiAction) {
        when (action) {
            is AddGoldSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddGoldSaleUiAction.OnCustomerSearchQueryChanged -> updateSearchQuery(action.query)
            is AddGoldSaleUiAction.OnCustomerSelected -> updateCustomer(action.customer)
            is AddGoldSaleUiAction.OnEditProduct -> updateProduct(action.product)
            is AddGoldSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddGoldSaleUiAction.OnDeleteProduct -> deleteProduct(action.product)
            AddGoldSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddGoldSaleUiAction.OnAddPayment -> addPayment(action.payment)
            is AddGoldSaleUiAction.OnEditPayment -> updatePayment(action.payment)
            is AddGoldSaleUiAction.OnRemovePayment -> removePayment(action.paymentId)
            is AddGoldSaleUiAction.OnUpdateProducts -> updateProductsSale(action.products)
            AddGoldSaleUiAction.OnDeleteAllProducts -> updateProductsSale(emptyList())
            else -> Unit
        }
    }

    private fun updateProductsSale(products: List<Product>) {
        _uiState.update { oldState ->
            oldState.copy(sale = oldState.sale.copy(products = products))
        }
    }

    private fun onSubmit() {
        Log.d(TAG, "onSubmit: ${uiState.value.sale}")
        if (uiState.value.sale.id.isNotBlank()) {
            updateSale()
        } else {
            addSale()
        }
    }

    private fun updateSale() {
        _uiState.update { oldState ->
            oldState.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            with(uiState.value) {
                val customer = selectedCustomer
                val updateLog = ChangeLog(
                    employeeId = currentUser.id,
                    employeeName = currentUser.fullName,
                    type = LogType.UPDATE
                )
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = uiState.value.sale.copy(
                            customerId = customer.id,
                            customerName = customer.name,
                            customerPhone = customer.phone,
                            paymentStatus = if ((uiState.value.sale.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID,
                            logs = oldState.sale.logs + updateLog
                        ),
                    )
                }
            }
            updateProductSaleUseCase(
                UpdateWholesaleRequest(
                    sale = uiState.value.sale,
                    payments = uiState.value.payments,
                    employeeName = uiState.value.currentUser.fullName,
                    employeeId = uiState.value.currentUser.id,
                    isAdmin = uiState.value.currentUser.role == UserRole.MANAGER
                )
            ).onSuccess {
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, isFinished = true)
                }
            }.onFailure {
                Log.e(TAG, "updateSale: ${it.message}", it)
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false)
                }
            }
        }
    }

    private fun addSale() {
        Log.d(TAG, "addSale: ${uiState.value.sale}")
        _uiState.update {
            it.copy(isLoading = true)
        }
        val customer = uiState.value.selectedCustomer
        val distributor = uiState.value.currentUser
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { oldState ->
                oldState.copy(
                    sale = oldState.sale.copy(
                        customerId = customer.id,
                        customerName = customer.name,
                        customerPhone = customer.phone,
                        distributorId = distributor.id,
                        distributorName = distributor.fullName,
                        createdAt = Date(),
                        paymentStatus = if ((uiState.value.sale.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID
                    )
                )
            }
            _uiState.update { oldState ->
                oldState.copy(
                    sale = oldState.sale.copy(
                        logs = oldState.sale.logs + ChangeLog(
                            employeeId = distributor.id,
                            employeeName = distributor.fullName,
                            type = LogType.CREATE
                        )
                    ),
                )
            }
            addProductSaleUseCase(
                AddWholesaleRequest(
                    sale = uiState.value.sale,
                    payments = uiState.value.payments,
                    isAdmin = uiState.value.currentUser.role == UserRole.MANAGER
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = oldState.sale.copy(id = id),
                        isLoading = false, isFinished = true
                    )
                }
                Log.d(TAG, "addSale success: $id")
            }.onFailure {
                Log.e(TAG, "addSale failed: ${it.message}", it)
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false)
                }
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    customerSearchQuery = query,
                )
            }
            fetchCustomersSuggestions()
        }
    }

    private fun fetchCustomersSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCustomersByNameUseCase(
                FetchWholesaleCustomersByNameRequest(
                    name = uiState.value.customerSearchQuery,
                    distributorId = ""
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

    private fun updateCustomer(customer: WholeSaleCustomer) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(selectedCustomer = customer)
            }
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                if (oldState.sale.products.any { it.name == product.name }) {
                    oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.map {
                        if (it.name == product.name) {
                            product
                        } else {
                            it
                        }
                    }))
                } else {
                    oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product))
                }
            }
            updateTotalAmounts()
        }
    }

    private fun updateTotalAmounts() {
        viewModelScope.launch(Dispatchers.Default) {
            val totalAmount = uiState.value.sale.products.sumOf { it.grams * it.gramPrice }
            val totalPaid =
                uiState.value.payments.filter { it.type != PaymentType.FUTURES }.sumOf { it.amount }
            _uiState.update {
                it.copy(totalPaid = totalPaid)
            }
            Log.d(TAG, "updateTotalAmounts: totalAmount: $totalAmount, totalPaid: $totalPaid")
        }
    }

    private fun addPayment(payment: Payment) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(payments = oldState.payments + payment)
            }
            updateTotalAmounts()
        }
    }


    private fun removePayment(paymentId: String) {
        Log.d(TAG, "paymentSent: $paymentId")
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(payments = oldState.payments.filter { it.id != paymentId })
            }
            updateTotalAmounts()
        }
    }

    private fun removeProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it.id != productId }))
            }
            updateTotalAmounts()
        }
    }

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it != product }))
            }
            updateTotalAmounts()
        }
    }

    private fun updatePayment(payment: Payment) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(payments = oldState.payments.map {
                    if (it.id == payment.id) {
                        payment
                    } else {
                        it
                    }
                })
            }
            updateTotalAmounts()
        }
    }


    private fun updateProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.map {
                    if (it.id == product.id) {
                        product
                    } else {
                        it
                    }
                }))
            }
            updateTotalAmounts()
        }
    }
}