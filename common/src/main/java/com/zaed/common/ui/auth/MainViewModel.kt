package com.zaed.common.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.User
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCurrentUserLoggedInStatusUseCase: GetCurrentUserLoggedInUseCase,
    private val logOutUseCase: LogoutUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()
    init {
        getCurrentUserLoggedInStatus()
    }

     fun signOut() {
        viewModelScope.launch (Dispatchers.IO){
            logOutUseCase().onSuccess {
                _uiState.update { it.copy(isSignedOut = true) }
            }.onFailure {
                it.printStackTrace()
            }
        }
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
    val loading: Boolean = true,
    val isSignedOut: Boolean = false
)
