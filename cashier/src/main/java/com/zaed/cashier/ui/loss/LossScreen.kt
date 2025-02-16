package com.zaed.cashier.ui.loss

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.ui.loss.component.LossScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun LossScreen(
    viewModel: LossViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (uiState.isSignedOut){
        if (uiState.isSignedOut) {
            onNavigateToLogin()
        }
    }
    LossScreenContent(
        uiState = uiState,
        onAction = viewModel::handleAction,
    )
}



