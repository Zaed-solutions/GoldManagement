package com.zaed.common.ui.suppliers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.domain.supplier.AddSupplierUseCase
import com.zaed.common.domain.supplier.DeleteSupplierUseCase
import com.zaed.common.domain.supplier.FetchSuppliersUseCase
import com.zaed.common.domain.supplier.UpdateSupplierUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuppliersViewModel(
    private val fetchSuppliersUseCase: FetchSuppliersUseCase,
    private val addSupplierUseCase: AddSupplierUseCase,
    private val updateSupplierUseCase: UpdateSupplierUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase
) : ViewModel() {
    private val TAG: String = "SuppliersViewModel"
    private val _uiState = MutableStateFlow(SuppliersUiState())
    val uiState = _uiState.asStateFlow()

    fun init(role: UserRole) {
        _uiState.update {
            it.copy(isAdmin = role == UserRole.MANAGER)
        }
        fetchSuppliers()
    }

    private fun fetchSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSuppliersUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allSuppliers = data
                        )
                    }
                    filterData()
                }.onFailure { e ->
                    Log.e(TAG, "fetchSuppliers: ${e.message}", e)
                }
            }
        }
    }

    private fun filterData() {
        viewModelScope.launch(Dispatchers.Default) {
            val filteredSuppliers = uiState.value.allSuppliers.filter { supplier ->
                listOf(supplier.name, supplier.phone).any {
                    it.contains(uiState.value.searchQuery, ignoreCase = true)
                }
            }
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = false,
                    filteredSuppliers = filteredSuppliers
                )
            }
        }
    }

    fun handleAction(action: SuppliersUiAction){
        when(action){
            is SuppliersUiAction.AddSupplier -> addSupplier(action.supplier)
            is SuppliersUiAction.DeleteSupplier -> deleteSupplier(action.supplier)
            is SuppliersUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            is SuppliersUiAction.UpdateSupplier -> updateSupplier(action.supplier)
            else -> Unit
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = true,
                    searchQuery = query
                )
            }
            filterData()
        }
    }

    private fun deleteSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            deleteSupplierUseCase(
                DeleteSupplierRequest(
                    supplierId = supplier.id
                )
            ).onSuccess {
                Log.d(TAG, "deleteSupplier: success")
            }.onFailure {
                Log.e(TAG, "deleteSupplier: ${it.message}",it )
            }
        }
    }

    private fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            updateSupplierUseCase(
                UpdateSupplierRequest(supplier)
            ).onSuccess {
                Log.d(TAG, "updateSupplier: success")
            }.onFailure {
                Log.e(TAG, "updateSupplier: ${it.message}", it)
            }
        }
    }

    private fun addSupplier(supplier: Supplier) {
        viewModelScope.launch (Dispatchers.IO){
            addSupplierUseCase(
                AddSupplierRequest(
                    supplier = supplier
                )
            ).onSuccess {
                Log.d(TAG, "addSupplier: success")
            }.onFailure {
                Log.e(TAG, "addSupplier: ${it.message}", it)
            }
        }
    }
}