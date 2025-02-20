package com.zaed.distributor.ui.addproductsale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.request.FetchWholesaleProductSaleRequest
import com.zaed.common.domain.FetchAllCategoriesUseCase
import com.zaed.common.domain.FetchWholesaleProductSaleUseCase
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductSaleViewModel(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val fetchProductSaleUseCase: FetchWholesaleProductSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    ): ViewModel() {
    private val TAG: String = "AddProductSaleVM"
    private val _uiState = MutableStateFlow(AddProductSaleUiState())
    val uiState = _uiState.asStateFlow()
    fun init(saleId: String){
        if(saleId.isNotBlank()){
            fetchSale(saleId)
        }
        fetchAllCategories()
        fetchCurrentUser()
    }

    private fun fetchSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            fetchProductSaleUseCase(
                FetchWholesaleProductSaleRequest(
                    saleId = saleId
                )
            ).onSuccess { data ->
                _uiState.update { oldState ->
                    oldState.copy(
                        sale = data
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
        TODO("Not yet implemented")
    }

    private fun fetchCustomer(customerId: String) {
        TODO("Not yet implemented")
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

    fun handleAction(action: AddProductSaleUiAction){
        when(action){
            is AddProductSaleUiAction.OnAddProduct -> addProduct(action.product)
            is AddProductSaleUiAction.OnCustomerSearchQueryChanged -> updateSearchQuery(action.query)
            is AddProductSaleUiAction.OnCustomerSelected -> updateCustomer(action.customer)
            is AddProductSaleUiAction.OnEditProduct -> updateProduct(action.product)
            is AddProductSaleUiAction.OnRemoveProduct -> removeProduct(action.productId)
            AddProductSaleUiAction.OnSubmitClicked -> onSubmit()
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
        TODO("Not yet implemented")
    }

    private fun addSale() {
        TODO("Not yet implemented")
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch (Dispatchers.IO){
            _uiState.update { oldState ->
                oldState.copy(customerSearchQuery = query)
            }
            fetchCustomerSuggestions()
        }
    }

    private fun fetchCustomerSuggestions() {
        viewModelScope.launch (Dispatchers.IO){
            // fetch customers based on search query
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
                oldState.copy(sale = oldState.sale.copy(products = oldState.sale.products + product))
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