package com.zaed.manager.ui.usermanagement

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserManagementViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: UserManagementUiAction){
        when(action){
            else -> Unit
        }
    }
}