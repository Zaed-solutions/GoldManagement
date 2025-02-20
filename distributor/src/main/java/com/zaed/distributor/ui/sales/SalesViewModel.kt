package com.zaed.distributor.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.domain.sale.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.DeleteWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.FetchDistributorSalesUseCase
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(
    private val fetchSalesUseCase: FetchDistributorSalesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteProductSaleUseCase: DeleteWholesaleProductSaleUseCase,
    private val deleteGoldSaleUseCase: DeleteWholesaleGoldSaleUseCase,
    private val logOutUseCase: LogoutUserUseCase
): ViewModel() {
    private val TAG = "SalesViewModel"
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchCurrentUser()
    }
    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                    fetchSales()
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchSales() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSalesUseCase(
                FetchDistributorSalesRequest(
                    distributorId = uiState.value.currentUser.id
                )
            ).collect{ result ->
                result.onSuccess { data ->
                    _uiState.update {oldState ->
                        oldState.copy(
                            allSales = data,
                        )
                    }
                    filterData()
                }.onFailure { e->
                    Log.e(TAG, "fetchSales: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleAction(action: SalesUiAction){
        when(action){
            is SalesUiAction.OnDeleteProductSale -> deleteProductSale(action.saleId)
            is SalesUiAction.OnDeleteGoldSale -> deleteGoldSale(action.saleId)
            SalesUiAction.OnSignOut -> signOut()
            is SalesUiAction.UpdatePaymentStatusFilter -> updatePaymentStatusFilter(action.status)
            is SalesUiAction.UpdateSearchQuery -> updateSearchQuery(action.searchQuery)
            else -> Unit
        }
    }

    private fun signOut() {
        viewModelScope.launch (Dispatchers.IO){
            logOutUseCase().onSuccess {
                _uiState.update { it.copy(isSignedOut = true) }
                Log.d(TAG, "signOut: success")
            }.onFailure {
                Log.e(TAG, "signOut: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun deleteProductSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            deleteProductSaleUseCase(
                DeleteWholesaleProductSaleRequest(
                    saleId = saleId,
                    distributorId = uiState.value.currentUser.id,
                    distributorName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteProductSale: success")
            }.onFailure {
                Log.e(TAG, "deleteProductSale: ${it.message}",it )
                it.printStackTrace()
            }
        }
    }

    private fun deleteGoldSale(saleId: String) {
        viewModelScope.launch (Dispatchers.IO){
            deleteGoldSaleUseCase(
                DeleteWholesaleGoldSaleRequest(
                    saleId = saleId,
                    distributorId = uiState.value.currentUser.id,
                    distributorName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteGoldSale: success")
            }.onFailure {
                Log.e(TAG, "deleteGoldSale: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun updateSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, searchQuery = searchQuery)
            }
            filterData(searchQuery = searchQuery)
        }
    }

    private fun updatePaymentStatusFilter(status: PaymentStatus) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, selectedPaymentStatus = status)
            }
            filterData(paymentStatus = status)
        }
    }

    fun filterData(
        searchQuery: String = uiState.value.searchQuery,
        paymentStatus: PaymentStatus = uiState.value.selectedPaymentStatus
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            if(searchQuery.isBlank() && paymentStatus == PaymentStatus.ALL){
                //no search query and payment status is all
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, displayedSales = oldState.allSales)
                }
            } else if(searchQuery.isBlank()) {
                //no search query & payment status is not all
                val paid = paymentStatus == PaymentStatus.PAID
                val filteredSales = uiState.value.allSales.filter { it.paid == paid }
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, displayedSales = filteredSales)
                }
            } else if(paymentStatus == PaymentStatus.ALL) {
                //search query is not blank & payment status is all
                val filteredSales = uiState.value.allSales.filter { it.customerName.contains(searchQuery, ignoreCase = true) }
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, displayedSales = filteredSales)
                }
            } else {
                //search query is not blank & payment status is not all
                val paid = paymentStatus == PaymentStatus.PAID
                val filteredSales = uiState.value.allSales.filter { it.customerName.contains(searchQuery, ignoreCase = true) && it.paid == paid }
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, displayedSales = filteredSales)
                }
            }
        }

    }
}