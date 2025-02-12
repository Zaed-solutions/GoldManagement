package com.zaed.common.ui.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.AuthenticationUiAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    role: UserRole,
    viewModel: SignUpViewModel = koinViewModel(),
    onBack: () -> Unit={},
    navigateToLogIn: () -> Unit={},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreenContent(
        role = role,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AuthenticationUiAction.OnBack -> onBack()
                AuthenticationUiAction.OnSignIn -> navigateToLogIn()
                else -> viewModel.handleAction(action)
            }
        }
    )
}


