package com.zaed.distributor.ui.addproductsale

import android.util.Log
import androidx.compose.ui.tooling.data.EmptyGroup.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            }.onFailure { e ->
                Log.e(TAG, "fetchSale: ${e.message}", e)
                e.printStackTrace()
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
            else -> Unit
        }
    }
}