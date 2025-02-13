package com.zaed.common.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.User
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.LogoutUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCurrentUserLoggedInStatusUseCase: GetCurrentUserLoggedInUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()
    init {
        getCurrentUserLoggedInStatus()
    }


    private fun getCurrentUserLoggedInStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInStatusUseCase().collect { result ->
                result.onSuccess { user ->
                    Log.d("MainViewModel", "getCurrentUserLoggedInStatus: $user")
                    _uiState.value = _uiState.value.copy(
                        currentUser = user,
                        loading = false
                    )
                }.onFailure {
                    Log.d("MainViewModel", "getCurrentUserLoggedInStatus: $it")
                    _uiState.value = _uiState.value.copy(
                        currentUser = null,
                        loading = false
                    )
                }
            }
        }
    }
}

data class MainUiState (
    val currentUser: User? = null,
    val loading: Boolean = true
)
