package com.zaed.manager.ui.stores

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.domain.store.AddStoreUseCase
import com.zaed.common.domain.store.DeleteStoreUseCase
import com.zaed.common.domain.store.GetStoresUseCase
import com.zaed.common.domain.store.UpdateStoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoresViewModel(
    private val getStoresUseCase: GetStoresUseCase,
    private val addStoreUseCase: AddStoreUseCase,
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase
): ViewModel() {
    private val TAG = "StoresViewModel"
    private val _uiState = MutableStateFlow(StoresUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchStores()
    }
    private fun fetchStores(){
        viewModelScope.launch (Dispatchers.IO){
            getStoresUseCase().onSuccess {  data ->
                _uiState.update { oldState -> oldState.copy(stores = data) }
            }.onFailure {
                Log.e(TAG, "fetchStores: ", )
            }
        }
    }

    fun handleAction(action: StoresUiAction) {
        when(action) {
            is StoresUiAction.OnAddStore -> addStore(action.store)
            is StoresUiAction.OnDeleteStore -> deleteStore(action.store)
            is StoresUiAction.OnUpdateStore -> updateStore(action.store)
            else -> Unit
        }
    }

    private fun addStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            addStoreUseCase(
                AddStoreRequest(
                    store = store
                )
            ).onSuccess {
                fetchStores()
            }.onFailure {
                Log.e(TAG, "addStore: ", )
            }
        }
    }

    private fun updateStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            updateStoreUseCase(
                UpdateStoreRequest(
                    store = store
                )
            ).onSuccess {
                fetchStores()
            }.onFailure {
                Log.e(TAG, "updateStore: ", )
            }
        }
    }

    private fun deleteStore(store: Store) {
        viewModelScope.launch (Dispatchers.IO){
            deleteStoreUseCase(
                DeleteStoreRequest(
                    store = store
                )
            ).onSuccess {
                fetchStores()
            }.onFailure {
                Log.e(TAG, "deleteStore: ", )
            }
        }
    }
}