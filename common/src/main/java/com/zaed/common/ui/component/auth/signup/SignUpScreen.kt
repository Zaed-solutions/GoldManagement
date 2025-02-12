package com.zaed.common.ui.component.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.component.auth.AuthenticationUiAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onBack: () -> Unit={},
    navigateToLogIn: () -> Unit={},
    onNavigateToPendingScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AuthenticationUiAction.OnBack -> onBack()
                AuthenticationUiAction.OnSignIn -> navigateToLogIn()
                AuthenticationUiAction.OnNavigateToPendingScreen -> onNavigateToPendingScreen()
                else -> viewModel.handleAction(action)
            }
        }
    )
}


