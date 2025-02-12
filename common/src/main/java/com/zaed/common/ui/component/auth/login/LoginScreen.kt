package com.zaed.common.ui.component.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.component.auth.AuthenticationUiAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    role: UserRole,
    viewModel: LoginViewModel = koinViewModel(),
    onBack: () -> Unit,
    navigateToSignUp: () -> Unit,
    onNavigateToPendingScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreenContent(
        role = role,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AuthenticationUiAction.OnBack -> onBack()
                AuthenticationUiAction.OnSignUp -> navigateToSignUp()
                AuthenticationUiAction.OnNavigateToPendingScreen -> onNavigateToPendingScreen()
                AuthenticationUiAction.OnNavigateToHomeScreen -> onNavigateToHomeScreen()
                else -> viewModel.handleAction(action)
            }
        }
    )
}
