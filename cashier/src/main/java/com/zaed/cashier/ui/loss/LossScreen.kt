package com.zaed.cashier.ui.loss

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.ui.loss.component.LossScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun LossScreen(
    viewModel: LossViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LossScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                LossUiAction.OnBack -> onBack()
                else -> viewModel.handleAction(action)
            }
        }
    )
}



