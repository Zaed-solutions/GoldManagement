package com.zaed.common.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.authentication.request.SignUpUserRequest
import com.zaed.common.data.model.store.Store
import com.zaed.common.domain.authentication.SignUpUserUseCase
import com.zaed.common.domain.store.GetStoresUseCase
import com.zaed.common.ui.auth.AuthenticationUiAction
import com.zaed.common.ui.auth.AuthenticationUiState
import com.zaed.common.ui.auth.FieldsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val getStoresUseCase: GetStoresUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getStores()
    }

    val stores = MutableStateFlow(SelectStoreUiState())

    private fun getStores() {
        viewModelScope.launch(Dispatchers.IO) {
            getStoresUseCase().collect { result ->
                result.onSuccess { data ->
                    stores.update {
                        it.copy(
                            stores = data
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = Exception(error.message))
                    }
                }
            }
        }
    }

    fun handleAction(action: AuthenticationUiAction) {
        when (action) {
            AuthenticationUiAction.OnSignUp -> signUp()
            is AuthenticationUiAction.OnUpdateFullName -> updateFullName(action.fullName)
            is AuthenticationUiAction.OnUpdatePassword -> updatePassword(action.password)
            is AuthenticationUiAction.OnUpdateUserName -> updateUserName(action.userName)
            is AuthenticationUiAction.OnUpdateStore -> updateStore(action.store)
            AuthenticationUiAction.ResetError -> resetError()
            is AuthenticationUiAction.OnUpdateRole -> updateRole(action.role)
            else -> {}
        }
    }

    private fun updateStore(store: Store) {
        stores.update {
            it.copy(selectedStore = store)
        }
    }

    private fun resetError() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun updateRole(role: UserRole) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(role = role)
            }
        }
    }

    private fun signUp() {
        if (!validInput()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val signUpUserRequest = SignUpUserRequest(
                fullName = uiState.value.fullName,
                userName = uiState.value.userName,
                password = uiState.value.password,
                role = uiState.value.role,
                store = stores.value.selectedStore!!
            )
            signUpUserUseCase(signUpUserRequest).onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        successMessage = "Sign Up Success"
                    )
                }
            }.onFailure { error ->
                if (error is SignUpError) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }

    private fun validInput(): Boolean {
        with(uiState.value) {
            if (fullName.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_FULL_NAME)
                }
                return false
            } else if (fullName.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_FULL_NAME)
                }
                return false
            } else if (userName.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_USER_NAME)
                }
                return false
            } else if (userName.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_USER_NAME)
                }
                return false
            } else if (password.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_PASSWORD)
                }
                return false
            } else if (password.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_PASSWORD)
                }
                return false
            } else if (stores.value.selectedStore.name.isEmpty() && uiState.value.role == UserRole.CASHIER) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_STORE)
                }
                return false
            } else {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.NONE)
                }
                return true
            }
        }
    }

    private fun updateUserName(userName: String) {
        _uiState.update {
            it.copy(userName = userName)
        }
    }

    private fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    private fun updateFullName(fullName: String) {
        _uiState.update {
            it.copy(fullName = fullName)
        }
    }
}


