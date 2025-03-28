package com.zaed.common.ui.addsilversale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.category.toCategory
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.inventory.InventoryType
import com.zaed.common.data.model.inventory.request.FetchInventoriesByTypeRequest
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.inventory.FetchInventoriesByTypeUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.AddWholesaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleUseCase
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddSilverSaleViewModel(
    private val fetchProductSaleUseCase: FetchWholesaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val getCurrentWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase,
    private val fetchMoneyPaymentsByIdsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val addProductSaleUseCase: AddWholesaleUseCase,
    private val fetchInventoryUseCase: FetchInventoriesByTypeUseCase,
    private val updateProductSaleUseCase: UpdateWholesaleUseCase
) : ViewModel() {
    private val TAG: String = "AddProductSaleVM"
    private val _uiState = MutableStateFlow(AddSilverSaleUiState())
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
                    id = saleId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        initialSale = data, sale = data
                    )
                }
                fetchCustomer(data.accountId)
                fetchPayments(data.paymentsIds)
            }.onFailure { e ->
                Log.e(TAG, "fetchSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun fetchInventories(ownerId: String) {
        Log.d(TAG, "fetchInventories: $ownerId")
        viewModelScope.launch(Dispatchers.IO) {
            fetchInventoryUseCase(
                FetchInventoriesByTypeRequest(
                    ownerId = ownerId,
                    inventoryType = InventoryType.SILVER
                )
            ).collect { result ->
                Log.d(TAG, "fetchInventories: $result")
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(categories = data.map { it.toCategory() })
                    }
                    Log.d(TAG, "fetchInventories: ${uiState.value.categories}")
                }.onFailure { e ->
                    Log.e(TAG, "fetchInventories: ${e.message}", e)
                    e.printStackTrace()
                }
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
                    oldState.copy(payments = data.filter { it !is FuturePayment })
                }

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
                    fetchInventories(data.id)
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

    fun handleAction(action: AddSilverSaleUiAction) {
        when (action) {
            is AddSilverSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddSilverSaleUiAction.OnCustomerSearchQueryChanged -> updateSearchQuery(action.query)
            is AddSilverSaleUiAction.OnCustomerSelected -> updateCustomer(action.customer)
            is AddSilverSaleUiAction.OnEditProduct -> updateProduct(action.product)
            is AddSilverSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddSilverSaleUiAction.OnDeleteProduct -> deleteProduct(action.product)
            AddSilverSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddSilverSaleUiAction.OnAddPayment -> addPayment(action.cashPayment)
            is AddSilverSaleUiAction.OnEditPayment -> updatePayment(action.cashPayment)
            is AddSilverSaleUiAction.OnRemovePayment -> removePayment(action.paymentId)
            is AddSilverSaleUiAction.OnUpdateProducts -> updateProductsSale(action.products)
            is AddSilverSaleUiAction.OnUpdateDiscount -> updateDiscount(action.discount)
            AddSilverSaleUiAction.OnDeleteAllProducts -> updateProductsSale(emptyList())
            else -> Unit
        }
    }

    private fun updateDiscount(discount: Double) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(discount = discount))
            }
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
                    type = com.zaed.common.data.model.authentication.LogType.UPDATE
                )
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = uiState.value.sale.copy(
                            accountId = customer.id,
                            customerName = customer.name,
                            customerPhone = customer.phone,
                            paymentStatus = if ((uiState.value.sale.totalAmount - uiState.value.totalPaid).toInt() <= 0) com.zaed.common.data.model.payment.PaymentStatus.PAID else com.zaed.common.data.model.payment.PaymentStatus.UNPAID,
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
                        accountId = customer.id,
                        customerName = customer.name,
                        customerPhone = customer.phone,
                        distributorId = distributor.id,
                        distributorName = distributor.fullName,
                        createdAt = Date(),
                        productType = ProductType.SILVER,
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
            val updatedProducts = uiState.value.sale.products.map {
                it.copy(buyingPrice = uiState.value.categories.firstOrNull()?.buyingPrice?:0.0)
            }
            addProductSaleUseCase(
                AddWholesaleRequest(
                    sale = uiState.value.sale.copy(products = updatedProducts),
                    payments = uiState.value.payments,
                    isAdmin = uiState.value.currentUser.role == UserRole.MANAGER
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = oldState.sale.copy(id = id), isLoading = false, isFinished = true
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
                val id = UUID.randomUUID().toString()
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product.copy(id = id)))
            }

        }
    }

    private fun addPayment(payment: Payment) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(payments = oldState.payments + payment)
            }

        }
    }


    private fun removePayment(paymentId: String) {
        Log.d(TAG, "paymentSent: $paymentId")
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(payments = oldState.payments.filter { it.id != paymentId })
            }

        }
    }

    private fun removeProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it.id != productId }))
            }

        }
    }

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it != product }))
            }

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

        }
    }
}