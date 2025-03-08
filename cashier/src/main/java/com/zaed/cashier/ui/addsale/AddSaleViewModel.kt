package com.zaed.cashier.ui.addsale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.sale.AddStoreSaleUseCase
import com.zaed.common.domain.sale.GetStoreSaleUseCase
import com.zaed.common.domain.sale.UpdateStoreSaleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddSaleViewModel(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val addSaleUseCase: AddStoreSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val updateSaleUseCase: UpdateStoreSaleUseCase,
    private val getSaleUseCase: GetStoreSaleUseCase
) : ViewModel() {
    private val TAG: String = "AddSaleViewModel"
    private val _uiState = MutableStateFlow(AddSaleUiState())
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
            getSaleUseCase(saleId).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(sale = data)
                }
            }.onFailure { e ->
                Log.e(TAG, "fetchSale: ${e.message}", e)
                e.printStackTrace()
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

    fun handleAction(action: AddSaleUiAction) {
        when (action) {
            AddSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddSaleUiAction.OnDeleteProduct -> deleteProduct(action.product)
            AddSaleUiAction.OnDeleteAllProducts -> deleteAllProducts()
            is AddSaleUiAction.OnUpdateProduct -> updateProduct(action.product)
            is AddSaleUiAction.OnUpdateCustomerName -> updateCustomerName(action.name)
            is AddSaleUiAction.OnUpdateCustomerPhone -> updateCustomerPhone(action.phone)
            is AddSaleUiAction.OnUpdateCustomerEmail -> updateCustomerEmail(action.email)
            else -> Unit
        }
    }

    private fun deleteAllProducts() {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = emptyList()))
            }
        }
    }

    private fun deleteProduct(product: Product) {
        _uiState.update { oldState ->
            oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it.id != product.id }))
        }
    }

    private fun onSubmit() {
        _uiState.update { it.copy(isLoading = true) }
        if (uiState.value.sale.id.isNotBlank()) {
            updateSale()
        } else {
            addSale()
        }
    }

    private fun updateSale() {
        viewModelScope.launch(Dispatchers.IO) {
            updateSaleUseCase(
                UpdateStoreSaleRequest(
                    sale = uiState.value.sale,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                _uiState.update { oldState ->
                    oldState.copy(
                        isFinished = true
                    )
                }
            }.onFailure { e ->
                Log.e(TAG, "updateSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun addSale() {
        viewModelScope.launch(Dispatchers.IO) {
            addSaleUseCase(
                AddStoreSaleRequest(
                    sale = uiState.value.sale.copy(
                        createdAt = Date(),
                        employeeName = uiState.value.currentUser.fullName,
                        employeeId = uiState.value.currentUser.id,
                        storeId = uiState.value.currentUser.storeId,
                        storeName = uiState.value.currentUser.storeName,
                        logs = listOf(
                            ChangeLog(
                                employeeId = uiState.value.currentUser.id,
                                employeeName = uiState.value.currentUser.fullName,
                                type = LogType.CREATE
                            )
                        )
                    )
                )
            ).onSuccess { id ->
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = oldState.sale.copy(id = id),
                        isFinished = true
                    )
                }
            }.onFailure { e ->
                Log.e(TAG, "addSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            Log.d(TAG, "addProduct: $product")
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product))
            }
            Log.d(TAG, "addProduct: ${_uiState.value.sale.products}")
        }
    }

    private fun updateCustomerPhone(customerPhoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerPhone = customerPhoneNumber))
            }
        }
    }

    private fun updateCustomerEmail(customerEmail: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerEmail = customerEmail))
            }
        }
    }

    private fun updateCustomerName(customerName: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerName = customerName))
            }
        }
    }
}
