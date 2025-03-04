package com.zaed.manager.ui.storedetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoreDetailsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StoreDetailsUiState())
    val uiState = _uiState.asStateFlow()
    fun init(storeId: String){

    }
    fun handleAction(action: StoreDetailsUiAction){

    }
}