package com.zaed.manager.ui.storedetails

import androidx.lifecycle.ViewModel
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoreDetailsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StoreDetailsUiState())
    val uiState = _uiState.asStateFlow()
    fun init(storeId: String){

    }
    fun handleAction(action: StoreDetailsUiAction){
        when(action){
            StoreDetailsUiAction.OnDeleteStore -> deleteStore()
            is StoreDetailsUiAction.OnFilterClicked -> filterSales(format = action.format)
            is StoreDetailsUiAction.OnInventoryQueryChanged -> filterInventory(action.query)
            is StoreDetailsUiAction.OnSalesQueryChanged -> filterSales(query = action.query)
            is StoreDetailsUiAction.OnUpdateStore -> updateStore(action.store)
            else -> Unit
        }
    }
    private fun filterInventory(query: String) {
        TODO("Not yet implemented")
    }
    private fun filterSales(
        format: DateFormat = uiState.value.selectedFilter,
        query: String = uiState.value.salesQuery
    ) {
        TODO("Not yet implemented")
    }

    private fun deleteStore() {
        TODO("Not yet implemented")
    }

    private fun updateStore(store: Store) {
        TODO("Not yet implemented")
    }
}