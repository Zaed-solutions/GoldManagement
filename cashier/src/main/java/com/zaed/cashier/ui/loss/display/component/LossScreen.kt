package com.zaed.cashier.ui.loss.display.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.ui.loss.display.LossScreenContent
import com.zaed.cashier.ui.loss.display.LossUiAction
import com.zaed.cashier.ui.loss.display.LossUiState
import com.zaed.cashier.ui.loss.display.LossViewModel
import com.zaed.cashier.ui.theme.CashierAppTheme
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


@Preview
@Composable
private fun LossScreenPreview() {
    CashierAppTheme {
        LossScreenContent(
            uiState = LossUiState(
                losses = listOf(
                    Loss(
                        id = "1",
                        value = 100.0,
                        reason = "fijnfinrifnrifnr"
                    ),
                    Loss(
                        id = "1",
                        value = 100.0,
                        reason = "fijnfinrifnrifnr"
                    ),
                    Loss(
                        id = "1",
                        value = 100.0,
                        reason = "fijnfinrifnrifnr"
                    ),
                    Loss(
                        id = "1",
                        value = 100.0,
                        reason = "fijnfinrifnrifnr"
                    ),
                    Loss(
                        id = "1",
                        value = 10000.0,
                        reason = "fijnfinrifnrifnr"
                    ),

                    )
            ),
            {}
        )
    }

}
