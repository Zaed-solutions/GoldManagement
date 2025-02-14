package com.zaed.cashier.ui.addsale

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.domain.AddStoreSaleUseCase
import com.zaed.common.domain.FetchAllProductsUseCase
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddSaleViewModel(
    private val fetchAllProductsUseCase: FetchAllProductsUseCase,
    private val addSaleUseCase: AddStoreSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase
): ViewModel() {
    private val TAG: String = "AddSaleViewModel"
    private val _uiState = MutableStateFlow(AddSaleUiState())
    val uiState = _uiState.asStateFlow()
    init{
        fetchAllProducts()
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch (Dispatchers.IO){
            getCurrentUserUseCase().collect{ result ->
                result.onSuccess { data ->
                    _uiState.update {oldState ->
                        oldState.copy(currentUser = data)
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchAllProducts() {
        viewModelScope.launch (Dispatchers.IO){
            fetchAllProductsUseCase().collect{ result ->
                result.onSuccess { data ->
                    _uiState.update {oldState ->
                        oldState.copy(allProducts = data)
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchAllProducts: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleAction(action: AddSaleUiAction){
        when(action){
            AddSaleUiAction.OnAddClicked -> addSale()
            is AddSaleUiAction.OnUpdateCustomerName -> updateCustomerName(action.customerName)
            is AddSaleUiAction.OnUpdateCustomerEmail -> updateCustomerEmail(action.customerEmail)
            is AddSaleUiAction.OnUpdateCustomerPhone -> updateCustomerPhone(action.customerPhoneNumber)
            is AddSaleUiAction.OnUpdateProductPrice -> updateProductPrice(action.productId, action.price)
            is AddSaleUiAction.OnUpdateProductQuantity -> updateProductQuantity(action.productId, action.quantity)
            is AddSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            is AddSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddSaleUiAction.OnUpdateDiscountType -> updateDiscountType(action.discountType)
            is AddSaleUiAction.OnUpdateDiscountValue -> updateDiscountValue(action.discountValue)
            else -> Unit
        }
    }

    private fun addSale() {
        viewModelScope.launch (Dispatchers.IO){
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
                _uiState.update{ oldState ->
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

    private fun updateProductPrice(productId: String, price: Double) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.map {
                    if (it.id == productId) {
                        it.copy(price = price)
                    } else {
                        it
                    }
                }))
            }
        }
    }

    private fun updateProductQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products.map {
                    if (it.id == productId) {
                        it.copy(quantity = quantity)
                    } else {
                        it
                    }
                }))
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
                oldState.copy(sale = oldState.sale.copy( discount = oldState.sale.discount.copy(type = discountType)))
            }
        }
    }

    private fun updateDiscountValue(discountValue: Double) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(sale = oldState.sale.copy( discount = oldState.sale.discount.copy(value = discountValue)))
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