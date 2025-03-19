package com.zaed.distributor.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.domain.sale.DeleteWholesaleUseCase
import com.zaed.common.domain.sale.FetchDistributorSalesUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.isAfter
import com.zaed.common.ui.util.isBefore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class SalesViewModel(
    private val fetchSalesUseCase: FetchDistributorSalesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val deleteProductSaleUseCase: DeleteWholesaleUseCase,
    private val deleteGoldSaleUseCase: DeleteWholesaleUseCase,
    private val convertDateFormatUseCase: ConvertSalesToDatedSalesUseCase,
    private val logOutUseCase: LogoutUserUseCase
) : ViewModel() {
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
            ).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSales = data,
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSales: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleAction(action: SalesUiAction) {
        when (action) {
            is SalesUiAction.OnDeleteProductSale -> deleteProductSale(action.saleId)
            is SalesUiAction.OnDeleteGoldSale -> deleteGoldSale(action.saleId)
            SalesUiAction.OnSignOut -> signOut()
            is SalesUiAction.UpdateDateFilter -> updateDateFilter(action.filter)
            is SalesUiAction.UpdateSearchQuery -> updateSearchQuery(action.searchQuery)
            is SalesUiAction.SetCustomRangeFilter -> setCustomRange(action.range)
            else -> Unit
        }
    }

    private fun setCustomRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, dateFilter = DateFormat.CUSTOM_RANGE, selectedRange = range)
            }
            filterData()
        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            deleteProductSaleUseCase(
                DeleteWholesaleRequest(
                    id = saleId,
                    distributorId = uiState.value.currentUser.id,
                    distributorName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                Log.d(TAG, "deleteProductSale: success")
            }.onFailure {
                Log.e(TAG, "deleteProductSale: ${it.message}", it)
                it.printStackTrace()
            }
        }
    }

    private fun deleteGoldSale(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteGoldSaleUseCase(
                DeleteWholesaleRequest(
                    id = saleId,
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
            filterData()
        }
    }

    private fun updateDateFilter(dateFilter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isLoading = true, dateFilter = dateFilter)
            }
            convertToDatedSales()
        }
    }

    private fun filterData() {
        val searchQuery: String = uiState.value.searchQuery
        val filter = uiState.value.dateFilter
        val range = uiState.value.selectedRange
        viewModelScope.launch(Dispatchers.Default) {
            if (searchQuery.isBlank()) {
                //no search query and payment status is all
                _uiState.update { oldState ->
                    oldState.copy(filteredSales = oldState.allSales)
                }
            } else {
                //search query is not blank & payment status is not all
                val filteredSales = uiState.value.allSales.filter {
                    listOf(
                        it.customerName,
                        it.receiptNumber
                    ).any {
                        it.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                }
                _uiState.update { oldState ->
                    oldState.copy(filteredSales = filteredSales)
                }
            }
            if(filter == DateFormat.CUSTOM_RANGE){
                val filteredSales = uiState.value.filteredSales.filter { sale ->
                    val afterFlag = range.first?.let { sale.createdAt.isAfter(it)  } ?: true
                    val beforeFlag = range.second?.let { sale.createdAt.isBefore(it)  } ?: true
                    afterFlag && beforeFlag
                }
                _uiState.update { oldState ->
                    oldState.copy(isLoading = false, filteredSales = filteredSales)
                }
            } else {
                convertToDatedSales()
            }
        }

    }

    private fun convertToDatedSales() {
        viewModelScope.launch(Dispatchers.Default) {
            val datedSales =
                convertDateFormatUseCase(uiState.value.filteredSales, uiState.value.dateFilter)
            _uiState.update { oldState ->
                oldState.copy(isLoading = false, datedSales = datedSales)
            }
        }
    }
}