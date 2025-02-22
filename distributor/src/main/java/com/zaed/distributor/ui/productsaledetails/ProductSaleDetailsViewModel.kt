package com.zaed.distributor.ui.productsaledetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomerSalesUseCase
import com.zaed.common.domain.payment.FetchPaymentsByIdsUseCase
import com.zaed.common.domain.sale.DeleteWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleProductSaleUseCase
import com.zaed.distributor.ui.saledetails.SaleDetailsUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductSaleDetailsViewModel(
    private val fetchSaleUseCase: FetchWholesaleProductSaleUseCase,
    private val fetchSalePaymentsUseCase: FetchPaymentsByIdsUseCase,
    private val getCurrentUseUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteWholesaleProductSaleUseCase: DeleteWholesaleProductSaleUseCase
): ViewModel() {
    private val TAG = "ProductSaleDetailsViewModel"
    private val _uiState = MutableStateFlow(ProductSaleDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun init(saleId: String){
        fetchSale(saleId)
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUseUseCase().collect { result ->
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

    private fun fetchSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSaleUseCase(
                FetchWholesaleProductSaleRequest(saleId)
            ).onSuccess { data ->
                _uiState.update { oldState->
                    oldState.copy(sale = data)
                }
                fetchPayments(data.paymentsIds)
            }.onFailure {
                Log.e(TAG, "fetchSale: ${it.message}", it)
            }
        }
    }

    private fun fetchPayments(paymentsIds: List<String>) {
        viewModelScope.launch (Dispatchers.IO){
            fetchSalePaymentsUseCase(
                FetchPaymentsByIdsRequest(paymentsIds)
            ).onSuccess { data ->
                _uiState.update { oldState->
                    oldState.copy(payments = data)
                }
            }.onFailure {
                Log.e(TAG, "fetchPayments: ${it.message}",it )
            }
        }
    }


    fun handleAction(action: ProductSaleDetailsUiAction){
        when(action){
            is ProductSaleDetailsUiAction.OnDeleteSale -> deleteSale()
            else -> Unit
        }
    }

    private fun deleteSale() {
        viewModelScope.launch (Dispatchers.IO){
            deleteWholesaleProductSaleUseCase(
                DeleteWholesaleProductSaleRequest(
                    saleId = uiState.value.sale.id
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(isSaleDeleted = true)
                }
            }.onFailure {
                Log.e(TAG, "deleteSale: ${it.message}", it)
            }
        }
    }
}