package com.zaed.common.ui.saledetails.cashiersaledetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.sale.DeleteStoreSaleUseCase
import com.zaed.common.domain.sale.GetStoreSaleUseCase
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SaleDetailsViewModel(
    private val getStoreSaleUseCase: GetStoreSaleUseCase,
    private val deleteStoreSaleUseCase: DeleteStoreSaleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleDetailsUiState())
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
                }.onFailure { e ->
                    Log.e("TAG", "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleAction(action: SaleDetailsUiAction) {
        when (action) {
            is SaleDetailsUiAction.OnUpdateStoreSale -> updateStoreSale(action.storeSale)
            is SaleDetailsUiAction.UpdateCustomerEmail -> updateCustomerEmail(action.email)
            is SaleDetailsUiAction.UpdateCustomerPhoneNumber -> updateCustomerPhoneNumber(action.phoneNumber)
            SaleDetailsUiAction.OnSubmit -> submit()
            SaleDetailsUiAction.ResetError -> resetError()
            SaleDetailsUiAction.OnDeleteSale -> deleteSale()
            else -> {}
        }
    }

    private fun deleteSale() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteStoreSaleUseCase(
                DeleteStoreSaleRequest(
                    saleId = uiState.value.storeSale.id,
                    employeeId = uiState.value.currentUser.id,
                    employeeName = uiState.value.currentUser.fullName
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(isSaleDeleted = true)
                }
            }.onFailure { e ->
                Log.e("TAG", "deleteSale: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    private fun updateCustomerEmail(email: String) {
        _uiState.update { it.copy(storeSale = it.storeSale.copy(customerEmail = email)) }
    }

    private fun updateCustomerPhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(storeSale = it.storeSale.copy(customerPhone = phoneNumber)) }
    }

    private fun updateStoreSale(storeSale: StoreTransaction) {
        _uiState.update { it.copy(storeSale = storeSale) }
    }

    private fun submit() {
        if (!validateInput()) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            // Call your use case or repository here
        }
    }

    private fun validateInput(): Boolean {
        return true
    }

    private fun resetError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun setSaleId(saleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            getStoreSaleUseCase(saleId).onSuccess { data ->
                _uiState.update { it.copy(storeSale = data) }
            }.onFailure {
                _uiState.update { it.copy(errorMessage = it.errorMessage) }
            }
        }

    }
}