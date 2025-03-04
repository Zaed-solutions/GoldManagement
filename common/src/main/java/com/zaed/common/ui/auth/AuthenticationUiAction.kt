package com.zaed.common.ui.auth

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.authentication.UserRole

sealed interface AuthenticationUiAction {
    data class OnUpdateFullName(val fullName: String) : AuthenticationUiAction
    data class OnUpdateUserName(val userName: String) : AuthenticationUiAction
    data class OnUpdatePassword(val password: String) : AuthenticationUiAction
    data class OnUpdateRole(val role: UserRole) : AuthenticationUiAction
    data class OnUpdateStore(val store: Store) : AuthenticationUiAction
    data object OnNavigateToHomeScreen : AuthenticationUiAction
    data object OnSignUp : AuthenticationUiAction
    data object OnSignIn : AuthenticationUiAction
    data object ResetError : AuthenticationUiAction
    data object OnBack : AuthenticationUiAction
}