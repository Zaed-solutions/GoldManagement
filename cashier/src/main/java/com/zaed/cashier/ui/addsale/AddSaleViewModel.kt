package com.zaed.cashier.ui.addsale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.UpdateStoreSaleRequest
import com.zaed.common.domain.AddStoreSaleUseCase
import com.zaed.common.domain.FetchAllCategoriesUseCase
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.GetStoreSaleUseCase
import com.zaed.common.domain.UpdateStoreSaleUseCase
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

    fun handleAction(action: AddSaleUiAction) {
        when (action) {
            AddSaleUiAction.OnSubmitClicked -> onSubmit()
            is AddSaleUiAction.OnUpdateCustomerName -> updateCustomerName(action.customerName)
            is AddSaleUiAction.OnUpdateCustomerEmail -> updateCustomerEmail(action.customerEmail)
            is AddSaleUiAction.OnUpdateCustomerPhone -> updateCustomerPhone(action.customerPhoneNumber)
            is AddSaleUiAction.OnEditProduct -> updateProduct(action.product)
            is AddSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddSaleUiAction.OnUpdateDiscountType -> updateDiscountType(action.discountType)
            is AddSaleUiAction.OnUpdateDiscountValue -> updateDiscountValue(action.discountValue)
            else -> Unit
        }
    }

    private fun onSubmit() {
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

    private fun addSale() {
        viewModelScope.launch(Dispatchers.IO) {
            addSaleUseCase(
                AddStoreSaleRequest(
                    sale = uiState.value.sale.copy(
                        createdAt = Date(),
                        employeeName = uiState.value.currentUser.fullName,
                        employeeId = uiState.value.currentUser.id,
                        storeId = uiState.value.currentUser.storeId,
                        storeName = uiState.value.currentUser.storeName
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


    private fun removeProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.filter { it.id != productId }))
            }
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product))
            }
        }
    }

    private fun updateDiscountType(discountType: DiscountType) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(discount = oldState.sale.discount.copy(type = discountType)))
            }
        }
    }

    private fun updateDiscountValue(discountValue: Double) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(discount = oldState.sale.discount.copy(value = discountValue)))
            }
        }
    }

    private fun updateCustomerPhone(customerPhoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(customerPhoneNumber = customerPhoneNumber))
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
