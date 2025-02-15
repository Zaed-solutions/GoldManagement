package com.zaed.cashier.ui.loss.component

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.cashier.ui.loss.LossFieldsError
import com.zaed.cashier.ui.loss.LossUiAction
import com.zaed.cashier.ui.loss.LossUiState
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.ui.components.AnimatedLoading
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.CustomSnackbar
import com.zaed.common.ui.components.TextInputTextField

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun LossScreenContent(
    uiState: LossUiState,
    onAction: (LossUiAction) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreateNewLossSheetOpen by remember { mutableStateOf(false) }
    var isDeleteLossSheetOpen by remember { mutableStateOf(false to "") }
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = context.getString(uiState.errorMessage),
                withDismissAction = true,
            )
            onAction(LossUiAction.ResetError)
        }
        if (uiState.successMessage != null) {
            isCreateNewLossSheetOpen = false
            snackbarHostState.showSnackbar(
                message = uiState.successMessage,
                withDismissAction = true,
            )

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
                .padding(it)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                )
                .padding(16.dp)
        ) {


            AnimatedLoading(uiState.isLoading)
            var selectedLoss by remember { mutableStateOf<String?>(null) }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.losses.keys.toList()) { date ->
                    val losses = uiState.losses[date] ?: emptyList()
                    key(date) {
                        AnimatedContent(
                            targetState = selectedLoss,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) with fadeOut(
                                    animationSpec = tween(
                                        300
                                    )
                                )

                            }
                        ) { targetDate ->
                            if (targetDate != date) {
                                MainContent(
                                    date = date,
                                    losses = losses,
                                    onShowDetails = {
                                        selectedLoss = date
                                    },
                                )
                            } else {
                                DetailsContent(
                                    onBack = {
                                        selectedLoss = null
                                    },
                                    date = date,
                                    losses = losses,
                                    onEdit = {
                                        //TODO
                                        Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                                    },
                                    onDelete = {
                                        isDeleteLossSheetOpen = true to it
                                    }
                                )
                            }

                        }
                    }
                }
            }
        }
        AnimatedBottomSheetWithCreateAndDismiss(
            isCreateNewLossSheetOpen,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            scope,
            onDismiss = {

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
                AnimatedLoading(uiState.isLoading)
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
                            isCreateNewLossSheetOpen = false
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }
        AnimatedVisibility(isDeleteLossSheetOpen.first) {
            ModalBottomSheet(
                onDismissRequest = {
                    isDeleteLossSheetOpen = false to ""
                },
            ) {
                ConfirmDeleteDialog(
                    onDismiss = {
                        isDeleteLossSheetOpen = false to ""
                    },
                    onConfirm = {
                        onAction(LossUiAction.OnDeleteLoss(isDeleteLossSheetOpen.second))
                        isDeleteLossSheetOpen = false to ""
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LossScreenPreview() {
    CashierAppTheme {
        LossScreenContent(
            uiState = LossUiState(
                losses = emptyMap()
            ),
            {}
        )
    }

}