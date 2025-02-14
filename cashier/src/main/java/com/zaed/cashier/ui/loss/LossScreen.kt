package com.zaed.cashier.ui.loss

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.ui.component.AnimatedLoading
import com.zaed.common.ui.component.BackIcon
import com.zaed.common.ui.component.CustomSnackbar
import com.zaed.common.ui.component.TextInputTextField
import com.zaed.common.ui.util.toMoneyFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LossScreenContent(
    uiState: LossUiState,
    onAction: (LossUiAction) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreateNewLossSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = context.getString(uiState.errorMessage.userMessage),
                withDismissAction = true,
            )
            onAction(LossUiAction.ResetError)
        }
        if (uiState.successMessage != null) {
            snackbarHostState.showSnackbar(
                message = uiState.successMessage,
                withDismissAction = true,
            )
            isCreateNewLossSheetOpen = false
            scope.launch {
                sheetState.hide()
            }
            onAction(LossUiAction.ResetSuccess)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Loss")
                },
                navigationIcon = {
                    BackIcon(onBack = { onAction(LossUiAction.OnBack) })
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                    isCreateNewLossSheetOpen = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                isError = uiState.errorMessage != null
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it)
        ) {
            AnimatedLoading(uiState.isLoading)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.losses) { loss ->
                    LossItem(loss = loss)
                }
            }
        }
        AnimatedBottomSheetWithCreateAndDissmis(
            isCreateNewLossSheetOpen,
            sheetState,
            scope,
            onDissmis = {
                scope.launch {
                    sheetState.hide()
                }
                isCreateNewLossSheetOpen = false
            }
        ) {
            var value by remember { mutableStateOf("") }
            var reason by remember { mutableStateOf("") }
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_new_loss),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = value,
                    onValueChange = { value = it },
                    label = stringResource(R.string.value),
                    withBorder = true,
                    errorMessage = uiState.fieldError.message,
                    isError = uiState.fieldError in listOf(
                        LossFieldsError.LOSS_VALUE_IS_EMPTY,
                        LossFieldsError.LOSS_VALUE_IS_INVALID
                    )
                )
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = reason,
                    onValueChange = { reason = it },
                    label = stringResource(R.string.reason),
                    withBorder = true,
                    errorMessage = uiState.fieldError.message,
                    isError = uiState.fieldError in listOf(
                        LossFieldsError.LOSS_REASON_IS_EMPTY
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onAction(
                                LossUiAction.OnCreateNewLoss(
                                    value = value,
                                    reason = reason
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(R.string.create))
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }
                            isCreateNewLossSheetOpen = false
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AnimatedBottomSheetWithCreateAndDissmis(
    isCreateNewLossSheetOpen: Boolean,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDissmis: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(isCreateNewLossSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                onDissmis()
            },
        ) {
            content()
        }
    }
}


@Composable
fun LossItem(loss: Loss) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = loss.value.toMoneyFormat(),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = loss.reason,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
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
