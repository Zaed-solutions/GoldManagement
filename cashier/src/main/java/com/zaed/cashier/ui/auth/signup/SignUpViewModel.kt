package com.zaed.cashier.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.UserRole
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.data.model.ui.AuthenticationUiAction
import com.zaed.common.data.model.ui.AuthenticationUiState
import com.zaed.common.data.model.ui.FieldsError
import com.zaed.common.data.model.ui.SignUpError
import com.zaed.common.domain.SignUpUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUserUseCase: SignUpUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: AuthenticationUiAction) {
        when (action) {
            AuthenticationUiAction.OnSignUp -> signUp()
            is AuthenticationUiAction.OnUpdateFullName -> updateFullName(action.fullName)
            is AuthenticationUiAction.OnUpdatePassword -> updatePassword(action.password)
            is AuthenticationUiAction.OnUpdateUserName -> updateUserName(action.userName)
            AuthenticationUiAction.ResetError -> resetError()
            else -> {}
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

    private fun signUp() {
        if(!validInput()){
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
                role = UserRole.CASHIER,
            )
            signUpUserUseCase(signUpUserRequest).collect { result ->
                result.onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            successMessage = "Sign Up Success"
                        )
                    }
                }.onFailure {error->
                    if(error is SignUpError) {
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
    }

    private fun validInput(): Boolean {
        with(uiState.value) {
            if (fullName.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_FULL_NAME)
                }
                return false
            }else if (fullName.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_FULL_NAME)
                }
                return false
            }else if (userName.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_USER_NAME)
                }
                return false
            }else if (userName.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_USER_NAME)
                }
                return false
            } else if (password.isEmpty()) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.EMPTY_PASSWORD)
                }
                return false
            }else if (password.length < 3) {
                _uiState.update {
                    it.copy(fieldsError = FieldsError.INVALID_PASSWORD)
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
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(userName = userName)
            }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(password = password)
            }
        }
    }

    private fun updateFullName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(fullName = fullName)
            }
        }
    }
}


