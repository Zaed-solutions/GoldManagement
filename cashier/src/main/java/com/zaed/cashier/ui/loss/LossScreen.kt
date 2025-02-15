package com.zaed.cashier.ui.loss

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.Loss
import org.koin.androidx.compose.koinViewModel

@Composable
fun LossScreen(
    viewModel: LossViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

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



