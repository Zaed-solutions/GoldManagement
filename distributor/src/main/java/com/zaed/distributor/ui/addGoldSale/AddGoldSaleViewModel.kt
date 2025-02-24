package com.zaed.distributor.ui.addGoldSale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleGoldSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.payment.FetchGoldPaymentsByIdsUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.sale.AddGoldSaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleGoldSaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class AddGoldSaleViewModel(
    private val fetchGoldSaleUseCase: FetchWholesaleGoldSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val getCurrentWholeSalesCustomerUseCase: GetWholeSalesCustomerUseCase,
    private val fetchCustomersByNameUseCase: FetchWholesaleCustomersByNameUseCase,
    private val fetchMoneyPaymentsByIdsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val fetchGoldPaymentsByIdsUseCase: FetchGoldPaymentsByIdsUseCase,
    private val addGoldSaleUseCase: AddGoldSaleUseCase,
    private val updateGoldSaleUseCase: UpdateWholesaleGoldSaleUseCase
) : ViewModel() {
    private val TAG = "GoldSaleViewModel"
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
            fetchGoldSaleUseCase(
                FetchWholesaleGoldSaleRequest(
                    saleId = saleId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        initialSale = data,
                        sale = data
                    )
                }
                fetchCustomer(data.customerId)
                fetchPayments(data.moneyPaymentsIds, data.goldPaymentsIds)
            }.onFailure { e ->
                Log.e(TAG, "fetchSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun fetchPayments(
        moneyPaymentsIds: List<String>,
        goldPaymentsIds: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchGoldPaymentsByIdsUseCase(
                FetchPaymentsByIdsRequest(goldPaymentsIds)
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(goldPayments = data)
                }
                Log.d(TAG, "fetchPayments: $data")
            }.onFailure {
                Log.e(TAG, "fetchPayments: ${it.message}", it)
            }
            fetchMoneyPaymentsByIdsUseCase(
                FetchPaymentsByIdsRequest(moneyPaymentsIds)
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(moneyPayments = data.filter { it.type != PaymentType.FUTURES })
                }
                Log.d(TAG, "fetchPayments: $data")
            }.onFailure {
                Log.e(TAG, "fetchPayments: ${it.message}", it)
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
                    "find the issue",
                    "fetchCurrentUser: $result"
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
            AddGoldSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddGoldSaleUiAction.OnAddPayment -> {
                when (action.moneyPayment) {
                    is MoneyPayment -> addPayment(action.moneyPayment)
                    is GoldPayment -> addGoldPayment(action.moneyPayment)
                }
            }

            is AddGoldSaleUiAction.OnEditPayment -> {
                when (action.moneyPayment) {
                    is MoneyPayment -> updatePayment(action.moneyPayment)
                    is GoldPayment -> updateGoldPayment(action.moneyPayment)
                }
            }

            is AddGoldSaleUiAction.OnRemovePayment -> removePayment(action.paymentId)
            else -> Unit
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
                    oldState.copy(
                        sale = uiState.value.sale.copy(
                            customerId = customer.id,
                            customerName = customer.name,
                            customerPhone = customer.phone,
                            paid = (uiState.value.totalPaid).toInt() <= 0,
                            logs = oldState.sale.logs + updateLog
                        ),
                        moneyPayments = oldState.moneyPayments.map { it.copy(customerId = oldState.selectedCustomer.id) }
                    )
                }
            }
            if (uiState.value.totalPaid != (uiState.value.totalAmount+uiState.value.laborCost)) {
                val futureMoneyPayment = MoneyPayment(
                    customerId = uiState.value.selectedCustomer.id,
                    type = PaymentType.FUTURES,
                    amount = (uiState.value.totalAmount - uiState.value.totalPaid)+uiState.value.laborCost
                )
                _uiState.update {oldState-> oldState.copy(moneyPayments = oldState.moneyPayments.filter { it.type!= PaymentType.FUTURES } + futureMoneyPayment) }
            }
            updateGoldSaleUseCase(
                UpdateWholesaleGoldSaleRequest(
                    sale = uiState.value.sale,
                    moneyPayments = uiState.value.moneyPayments,
                    goldPayments = uiState.value.goldPayments,
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
        Log.d(TAG, "addSale: Starting sale addition process")
        _uiState.update {
            Log.d(TAG, "addSale: Setting isLoading to true")
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val customer = uiState.value.selectedCustomer
            val distributor = uiState.value.currentUser
            Log.d(TAG, "addSale: Customer=${customer.name}, Distributor=${distributor.fullName}")
            _uiState.update { oldState ->
                Log.d(TAG, "addSale: Updating sale with customer and distributor details")
                oldState.copy(
                    sale = oldState.sale.copy(
                        customerId = customer.id,
                        customerName = customer.name,
                        customerPhone = customer.phone,
                        distributorId = distributor.id,
                        distributorName = distributor.fullName,
                        createdAt = Date(),
                        paid = (oldState.totalPaid).toInt() <= 0
                    )
                )
            }
            _uiState.update { oldState ->
                Log.d(TAG, "addSale: Adding creation log to sale")
                oldState.copy(
                    sale = oldState.sale.copy(
                        logs = oldState.sale.logs + ChangeLog(
                            employeeId = distributor.id,
                            employeeName = distributor.fullName,
                            action = "created this sale"
                        )
                    ),
                    moneyPayments = oldState.moneyPayments.map { it.copy(customerId = oldState.selectedCustomer.id) },
                    goldPayments = oldState.goldPayments.map { it.copy(customerId = oldState.selectedCustomer.id) }
                )
            }
            if (uiState.value.totalPaid != (uiState.value.totalAmount+uiState.value.laborCost)) {
                val futureMoneyPayment = MoneyPayment(
                    customerId = uiState.value.selectedCustomer.id,
                    type = PaymentType.FUTURES,
                    amount = (uiState.value.totalAmount - uiState.value.totalPaid)+uiState.value.laborCost
                )
                _uiState.update {oldState-> oldState.copy(moneyPayments = oldState.moneyPayments.filter { it.type!= PaymentType.FUTURES } + futureMoneyPayment) }
            }
            Log.d(TAG, "addSale: Calling addGoldSaleUseCase with sale and payments")
            addGoldSaleUseCase(
                AddWholesaleGoldSaleRequest(
                    sale = uiState.value.sale,
                    moneyPayments = uiState.value.moneyPayments,
                    goldPayments = uiState.value.goldPayments
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    Log.d(
                        TAG,
                        "addSale: Successfully added sale with ID=$id, setting isLoading=false, isFinished=true"
                    )
                    oldState.copy(
                        sale = oldState.sale.copy(id = id),
                        isLoading = false,
                        isFinished = true
                    )
                }
            }.onFailure {
                Log.e(TAG, "addSale: Failed to add sale - ${it.message}", it)
                _uiState.update { oldState ->
                    Log.d(TAG, "addSale: Setting isLoading to false due to failure")
                    oldState.copy(isLoading = false)
                }
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        Log.d(TAG, "updateSearchQuery: Updating search query to '$query'")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "updateSearchQuery: Setting customerSearchQuery to '$query'")
                oldState.copy(customerSearchQuery = query)
            }
            Log.d(TAG, "updateSearchQuery: Triggering fetchCustomersSuggestions")
            fetchCustomersSuggestions()
        }
    }

    private fun fetchCustomersSuggestions() {
        Log.d(TAG, "fetchCustomersSuggestions: Starting to fetch customer suggestions")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                TAG,
                "fetchCustomersSuggestions: Fetching customers with query '${uiState.value.customerSearchQuery}'"
            )
            fetchCustomersByNameUseCase(
                FetchWholesaleCustomersByNameRequest(
                    name = uiState.value.customerSearchQuery
                )
            ).onSuccess { data ->
                withContext(Dispatchers.Main) {
                    _uiState.update { oldState ->
                        Log.d(
                            TAG,
                            "fetchCustomersSuggestions: Successfully fetched ${data.size} customers, updating suggestedCustomers"
                        )
                        oldState.copy(suggestedCustomers = data)
                    }
                }
            }.onFailure { e ->
                Log.e(TAG, "fetchCustomersSuggestions: Failed to fetch customers - ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun updateCustomer(customer: WholeSaleCustomer) {
        Log.d(TAG, "updateCustomer: Updating selected customer to ${customer.name}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "updateCustomer: Setting selectedCustomer to ${customer.name}")
                oldState.copy(selectedCustomer = customer)
            }
        }
    }

    private fun addProduct(product: Product) {
        Log.d(TAG, "addProduct: Adding product with ID=${product.grams * product.gramPrice}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product))
            }
            Log.d(
                TAG,
                "addProduct: Product added successfully with ID=${uiState.value.totalAmount}"
            )
            Log.d(
                TAG,
                "addProduct: Product added successfully with ID=${uiState.value.sale.products.sumOf { it.grams * it.gramPrice }}"
            )
        }
    }


    private fun addPayment(moneyPayment: MoneyPayment) {
        Log.d(TAG, "addPayment: Adding payment with ID=${moneyPayment.id}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "addPayment: Adding payment ${moneyPayment.id} to moneyPayments list")
                oldState.copy(moneyPayments = oldState.moneyPayments + moneyPayment)
            }
        }
    }

    private fun addGoldPayment(moneyPayment: GoldPayment) {
        Log.d(TAG, "addPayment: Adding payment with ID=${moneyPayment.id}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "addPayment: Adding payment ${moneyPayment.id} to moneyPayments list")
                oldState.copy(goldPayments = oldState.goldPayments + moneyPayment)
            }
        }
    }

    private fun removePayment(paymentId: String) {
        Log.d(TAG, "removePayment: Removing payment with ID=$paymentId")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "removePayment: Filtering out payment $paymentId from moneyPayments")
                oldState.copy(moneyPayments = oldState.moneyPayments.filter { it.id != paymentId }) // ملاحظة: يبدو أن هناك خطأ في الفلترة، يجب أن يكون !=
            }
        }
    }

    private fun removeProduct(productId: String) {
        Log.d(TAG, "removeProduct: Removing product with ID=$productId")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "removeProduct: Filtering out product $productId from sale products")
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it.id != productId }))
            }
        }
    }

    private fun updatePayment(moneyPayment: MoneyPayment) {
        Log.d(TAG, "updatePayment: Updating payment with ID=${moneyPayment.id}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(
                    TAG,
                    "updatePayment: Replacing payment ${moneyPayment.id} in moneyPayments list"
                )
                oldState.copy(
                    moneyPayments = oldState.moneyPayments.map {
                        if (it.id == moneyPayment.id) moneyPayment else it
                    }
                )
            }
        }
    }

    private fun updateGoldPayment(goldPayment: GoldPayment) {
        Log.d(TAG, "updatePayment: Updating payment with ID=${goldPayment}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    goldPayments = oldState.goldPayments.map {
                        if (it.id == goldPayment.id) goldPayment else it
                    }
                )
            }
        }
    }

    private fun updateProduct(product: Product) {
        Log.d(TAG, "updateProduct: Updating product with ID=${product.id}")
        viewModelScope.launch {
            _uiState.update { oldState ->
                Log.d(TAG, "updateProduct: Replacing product ${product.id} in sale products list")
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.map {
                    if (it.id == product.id) product else it
                }))
            }
        }
    }
}