package com.zaed.distributor.ui.addproductsale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleProductSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.AddWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleProductSaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddProductSaleViewModel(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchProductSaleUseCase: FetchWholesaleProductSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val getCurrentWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase,
    private val fetchMoneyPaymentsByIdsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val addProductSaleUseCase: AddWholesaleProductSaleUseCase,
    private val updateProductSaleUseCase: UpdateWholesaleProductSaleUseCase
) : ViewModel() {
    private val TAG: String = "AddProductSaleVM"
    private val _uiState = MutableStateFlow(AddProductSaleUiState())
    val uiState = _uiState.asStateFlow()
    fun init(saleId: String) {
        if (saleId.isNotBlank()) {
            fetchSale(saleId)
        }
        fetchAllCategories()
        fetchCurrentUser()
    }

    private fun fetchSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchProductSaleUseCase(
                FetchWholesaleProductSaleRequest(
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
                    oldState.copy(moneyPayments = data.filter { it.type != PaymentType.FUTURES })
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

    private fun fetchAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllCategoriesUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(categories = data)
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchAllCategories: ${e.message}", e)
                    e.printStackTrace()
                }
                Log.d(
                    "find the issue", "fetchAllCategories: $result"
                )
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

    fun handleAction(action: AddProductSaleUiAction) {
        when (action) {
            is AddProductSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddProductSaleUiAction.OnCustomerSearchQueryChanged -> updateSearchQuery(action.query)
            is AddProductSaleUiAction.OnCustomerSelected -> updateCustomer(action.customer)
            is AddProductSaleUiAction.OnEditProduct -> updateProduct(action.product)
            is AddProductSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddProductSaleUiAction.OnDeleteProduct -> deleteProduct(action.product)
            AddProductSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddProductSaleUiAction.OnAddPayment -> addPayment(action.moneyPayment)
            is AddProductSaleUiAction.OnEditPayment -> updatePayment(action.moneyPayment)
            is AddProductSaleUiAction.OnRemovePayment -> removePayment(action.paymentId)
            is AddProductSaleUiAction.OnUpdateProducts -> updateProductsSale(action.products)
            AddProductSaleUiAction.OnDeleteAllProducts -> updateProductsSale(emptyList())
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
                    action = "updated this sale"
                )
                _uiState.update { oldState ->
                    oldState.copy(sale = uiState.value.sale.copy(
                        customerId = customer.id,
                        customerName = customer.name,
                        customerPhone = customer.phone,
                        paymentStatus = if ((uiState.value.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID,
                        logs = oldState.sale.logs + updateLog
                    ),
                        moneyPayments = oldState.moneyPayments.map { it.copy(customerId = oldState.selectedCustomer.id) })
                }
            }
            if (uiState.value.totalPaid != uiState.value.totalAmount) {
                val futureMoneyPayment = MoneyPayment(
                    customerId = uiState.value.selectedCustomer.id,
                    type = PaymentType.FUTURES,
                    amount = uiState.value.totalAmount - uiState.value.totalPaid
                )
                _uiState.update { oldState -> oldState.copy(moneyPayments = oldState.moneyPayments.filter { it.type != PaymentType.FUTURES } + futureMoneyPayment) }
            }
            updateProductSaleUseCase(
                UpdateWholesaleProductSaleRequest(
                    sale = uiState.value.sale,
                    moneyPayments = uiState.value.moneyPayments,
                    employeeName = uiState.value.currentUser.fullName,
                    employeeId = uiState.value.currentUser.id
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
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val customer = uiState.value.selectedCustomer
            val distributor = uiState.value.currentUser
            _uiState.update { oldState ->
                oldState.copy(
                    sale = oldState.sale.copy(
                        customerId = customer.id,
                        customerName = customer.name,
                        customerPhone = customer.phone,
                        distributorId = distributor.id,
                        distributorName = distributor.fullName,
                        createdAt = Date(),
                        paymentStatus = if ((uiState.value.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID
                    )
                )
            }
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(
                    logs = oldState.sale.logs + ChangeLog(
                        employeeId = distributor.id,
                        employeeName = distributor.fullName,
                        action = "created this sale"
                    )
                ),
                    moneyPayments = oldState.moneyPayments.map { it.copy(customerId = oldState.selectedCustomer.id) })
            }
            if (uiState.value.totalPaid != uiState.value.totalAmount) {
                val futureMoneyPayment = MoneyPayment(
                    customerId = customer.id,
                    type = PaymentType.FUTURES,
                    amount = uiState.value.totalAmount - uiState.value.totalPaid
                )
                _uiState.update { it.copy(moneyPayments = it.moneyPayments + futureMoneyPayment) }
            }
            addProductSaleUseCase(
                AddWholesaleProductSaleRequest(
                    sale = uiState.value.sale, moneyPayments = uiState.value.moneyPayments
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = oldState.sale.copy(id = id), isLoading = false, isFinished = true
                    )
                }
            }.onFailure {
                Log.e(TAG, "addSale: ${it.message}", it)
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
                    distributorId = uiState.value.currentUser.id
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
                    }
                    )
                    )
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
            val totalPaid = uiState.value.moneyPayments.filter { it.type != PaymentType.FUTURES }
                .sumOf { it.amount }
            _uiState.update {
                it.copy(totalAmount = totalAmount, totalPaid = totalPaid)
            }
            Log.d(TAG, "updateTotalAmounts: totalAmount: $totalAmount, totalPaid: $totalPaid")
        }
    }

    private fun addPayment(moneyPayment: MoneyPayment) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(moneyPayments = oldState.moneyPayments + moneyPayment)
            }
            updateTotalAmounts()
        }
    }

    private fun removePayment(paymentId: String) {
        Log.d(TAG, "paymentSent: $paymentId")
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(moneyPayments = oldState.moneyPayments.filter { it.id != paymentId })
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

    private fun updatePayment(moneyPayment: MoneyPayment) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(moneyPayments = oldState.moneyPayments.map {
                    if (it.id == moneyPayment.id) {
                        moneyPayment
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