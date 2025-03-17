package com.zaed.common.ui.addpurchase

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.category.AddCategoryUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.customer.FetchSuppliersByNameUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.purchase.FetchPurchaseUseCase
import com.zaed.common.domain.sale.AddPurchaseUseCase
import com.zaed.common.domain.sale.UpdatePurchaseUseCase
import com.zaed.common.domain.supplier.AddSupplierUseCase
import com.zaed.common.domain.supplier.FetchSupplierUseCase
import com.zaed.common.domain.supplier.FetchSuppliersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddPurchaseViewModel(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchPurchaseUseCase: FetchPurchaseUseCase,
    private val fetchSuppliersUseCase: FetchSuppliersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val getSupplierUseCase : FetchSupplierUseCase,
    private val fetchSupplierByNameUseCase: FetchSuppliersByNameUseCase,
    private val fetchMoneyPaymentsByIdsUseCase: FetchMoneyPaymentsByIdsUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase,
    private val updatePurchaseUseCase: UpdatePurchaseUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addSupplierUseCase: AddSupplierUseCase
) : ViewModel() {
    private val TAG: String = "AddProductSaleVM"
    private val _uiState = MutableStateFlow(AddPurchaseUiState())
    val uiState = _uiState.asStateFlow()
    fun init(saleId: String) {
        if (saleId.isNotBlank()) {
            fetchPurchase(saleId)
        }
        fetchAllCategories()
        fetchCurrentUser()
        fetchSuppliers()
    }

    private fun fetchPurchase(purchaseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPurchaseUseCase(
                    purchaseId
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        initialPurchase = data,
                        purchase = data
                    )
                }
                fetchSupplier(data.customerId)
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
    private fun fetchSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSuppliersUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSuppliers = data,
                            suggestedSuppliers = data
                        )
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchSuppliers: ${e.message}", e)
                }
            }
        }
    }
    private fun fetchSupplier(supplierId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSupplierUseCase(
                FetchSupplierRequest(
                    supplierId = supplierId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        selectedSupplier = data
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

    fun handleAction(action: AddPurchaseUiAction) {
        when (action) {
            is AddPurchaseUiAction.OnAddProduct -> addProduct(action.product)
            is AddPurchaseUiAction.OnSupplierSearchQueryChanged -> updateSearchQuery(action.query)
            is AddPurchaseUiAction.OnSupplierSelected -> updateSupplier(action.supplierId)
            is AddPurchaseUiAction.OnEditProduct -> updateProduct(action.product)
            is AddPurchaseUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddPurchaseUiAction.OnDeleteProduct -> deleteProduct(action.product)
            AddPurchaseUiAction.OnSubmitClicked -> onSubmit()
            is AddPurchaseUiAction.OnAddPayment -> addPayment(action.cashPayment)
            is AddPurchaseUiAction.OnEditPayment -> updatePayment(action.cashPayment)
            is AddPurchaseUiAction.OnRemovePayment -> removePayment(action.paymentId)
            is AddPurchaseUiAction.OnUpdateProducts -> updateProductsPurchase(action.products)
            AddPurchaseUiAction.OnDeleteAllProducts -> updateProductsPurchase(emptyList())
            is AddPurchaseUiAction.OnAddNewCategory -> addCategory(action.category)
            is AddPurchaseUiAction.OnAddNewSupplierClicked -> addSupplier(action.supplier)
            is AddPurchaseUiAction.OnProductTypeSelected -> {
                _uiState.update {
                    it.copy(
                    )
                }
            }
            AddPurchaseUiAction.ReselectProductType -> {
                _uiState.update { oldState ->
                    oldState.copy(
                        payments = emptyList(),
                        purchase = oldState.purchase.copy(products = emptyList()),
                    )
                }
            }
            else -> Unit
        }
    }

    private fun addSupplier(supplier: Supplier) {
        viewModelScope.launch(Dispatchers.IO) {
            addSupplierUseCase(
                request = AddSupplierRequest(
                    supplier = supplier,
                )
            ).onSuccess {
                Log.d(TAG, "addSupplier: $it")
            }.onFailure {
                Log.e(TAG, "addSupplier: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun addCategory(category: Category) {
        viewModelScope.launch (Dispatchers.IO){
            addCategoryUseCase(category).onSuccess {
                Log.d(TAG, "addCategory: $it")
            }.onFailure {
                Log.e(TAG, "addCategory: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun updateProductsPurchase(products: List<Product>) {
        _uiState.update { oldState ->
            oldState.copy(purchase = oldState.purchase.copy(products = products))
        }
    }

    private fun onSubmit() {
        if (uiState.value.purchase.id.isNotBlank()) {
            updatePurchase()
        } else {
            addPurchase()
        }
    }

    private fun updatePurchase() {
        _uiState.update { oldState ->
            oldState.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            with(uiState.value) {
                val customer = selectedSupplier
                val updateLog = ChangeLog(
                    employeeId = currentUser.id,
                    employeeName = currentUser.fullName,
                    type = LogType.UPDATE
                )
                _uiState.update { oldState ->
                    oldState.copy(
                        purchase = uiState.value.purchase.copy(
                            supplierId = customer.id,
                            supplierName = customer.name,
                            supplierPhone = customer.phone,
                            paymentStatus = if ((uiState.value.purchase.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID,
                            logs = oldState.purchase.logs + updateLog
                        ),
                    )
                }
            }
            updatePurchaseUseCase(
                UpdatePurchaseRequest(
                    purchase = uiState.value.purchase,
                    payments = uiState.value.payments,
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

    private fun addPurchase() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        val customer = uiState.value.selectedSupplier
        val distributor = uiState.value.currentUser
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { oldState ->
                oldState.copy(
                    purchase = oldState.purchase.copy(
                        supplierId = customer.id,
                        supplierName = customer.name,
                        supplierPhone = customer.phone,
                        distributorId = distributor.id,
                        distributorName = distributor.fullName,
                        createdAt = Date(),
                        paymentStatus = if ((uiState.value.purchase.totalAmount - uiState.value.totalPaid).toInt() <= 0) PaymentStatus.PAID else PaymentStatus.UNPAID
                    )
                )
            }
            _uiState.update { oldState ->
                oldState.copy(
                    purchase = oldState.purchase.copy(
                        logs = oldState.purchase.logs + ChangeLog(
                            employeeId = distributor.id,
                            employeeName = distributor.fullName,
                            type = LogType.CREATE
                        )
                    ),
                )
            }
            addPurchaseUseCase(
                AddPurchaseRequest(
                    purchase = uiState.value.purchase,
                    payments = uiState.value.payments,
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    oldState.copy(
                        purchase = oldState.purchase.copy(id = id), isLoading = false, isFinished = true
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
                    supplierSearchQuery = query,
                    suggestedSuppliers =if(query.isNotBlank()) uiState.value.allSuppliers.filter { it.name.contains(query) } else uiState.value.allSuppliers
                )
            }

        }
    }


    private fun updateSupplier(supplierId: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(selectedSupplier = uiState.value.allSuppliers.first { it.id == supplierId })
            }
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                if (oldState.purchase.products.any { it.name == product.name }) {
                    oldState.copy(purchase = oldState.purchase.copy(products = oldState.purchase.products.map {
                        if (it.name == product.name) {
                            product
                        } else {
                            it
                        }
                    }
                    )
                    )
                } else {
                    oldState.copy(purchase = oldState.purchase.copy(products = oldState.purchase.products + product))
                }
            }
            updateTotalAmounts()
        }
    }

    private fun updateTotalAmounts() {
        viewModelScope.launch(Dispatchers.Default) {
            val totalAmount = uiState.value.purchase.products.sumOf { it.grams * it.gramPrice }
            val totalPaid = uiState.value.payments.filter { it.type != PaymentType.FUTURES }
                .sumOf { it.amount }
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
                oldState.copy(purchase = oldState.purchase.copy(products = oldState.purchase.products.filter { it.id != productId }))
            }
            updateTotalAmounts()
        }
    }

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(purchase = oldState.purchase.copy(products = oldState.purchase.products.filter { it != product }))
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
                oldState.copy(purchase = oldState.purchase.copy(products = oldState.purchase.products.map {
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