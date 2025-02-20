package com.zaed.cashier.ui.loss.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.cashier.ui.loss.LossFieldsError
import com.zaed.cashier.ui.loss.LossUiAction
import com.zaed.cashier.ui.loss.LossUiState
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.ui.components.AnimatedLoading
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.CustomSnackbar
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.components.NumberInputTextField
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
    var selectedLoss by remember { mutableStateOf<Loss>(Loss()) }
    var isSaveLossSheetOpen by remember { mutableStateOf(false) }
    var isDeleteLossSheetOpen by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = context.getString(uiState.errorMessage),
                withDismissAction = true,
            )
            onAction(LossUiAction.ResetError)
        }
        if (uiState.successMessage != null) {
            selectedLoss = Loss()
            isSaveLossSheetOpen = false
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
                    Text(text = stringResource(R.string.losses))
                },
                actions = {
                    MoreDropDownMenu(
                        items = listOf(
                            MoreDropdownItem(
                                onClick = { onAction(LossUiAction.OnSignOut) },
                                title = stringResource(R.string.sign_out),
                                icon = Icons.AutoMirrored.Filled.Logout,
                                tint = MaterialTheme.colorScheme.error
                            )
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedLoss = Loss()
                    isSaveLossSheetOpen = true
                }
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                isError = uiState.errorMessage != null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AnimatedLoading(uiState.isLoading)
            var selectedDate by remember { mutableStateOf<String?>(null) }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.losses.keys.toList()) { date ->
                    key(date) {
                        AnimatedContent(
                            targetState = selectedDate,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                                    animationSpec = tween(
                                        300
                                    )
                                )

                            }
                        ) { targetDate ->
                            if (targetDate != date) {
                                MainContent(
                                    date = date,
                                    losses = uiState.losses[date] ?: emptyList(),
                                    onShowDetails = {
                                        selectedDate = date
                                    },
                                )
                            } else {
                                DetailsContent(
                                    onBack = {
                                        selectedDate = null
                                    },
                                    date = date,
                                    losses = uiState.losses[date] ?: emptyList(),
                                    onEdit = { loss ->
                                        selectedLoss = loss
                                        isSaveLossSheetOpen = true
                                    },
                                    onDelete = { loss ->
                                        selectedLoss = loss
                                        isDeleteLossSheetOpen = true
                                    }
                                )
                            }

                        }
                    }
                }
            }
        }
        AnimatedBottomSheetWithCreateAndDismiss(
            isSaveLossSheetOpen,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            scope,
            onDismiss = {
                selectedLoss = Loss()
                isSaveLossSheetOpen = false
            }
        ) {
            AddLossBottomSheetContent(
                isLoading = uiState.isLoading,
                fieldError = uiState.fieldError,
                initialLoss = selectedLoss,
                onSaveLoss = {
                    onAction(
                        if (selectedLoss.id.isEmpty()) {
                            LossUiAction.OnCreateLoss(it)
                        } else {
                            LossUiAction.OnUpdateLoss(it)
                        }
                    )
                },
                onDismiss = {
                    selectedLoss = Loss()
                    isSaveLossSheetOpen = false
                }
            )
        }
        AnimatedVisibility(isDeleteLossSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedLoss = Loss()
                    isDeleteLossSheetOpen = false
                },
            ) {
                ConfirmDeleteDialog(
                    onDismiss = {
                        selectedLoss = Loss()
                        isDeleteLossSheetOpen = false
                    },
                    label = stringResource(R.string.loss),
                    onConfirm = {
                        onAction(LossUiAction.OnDeleteLoss(selectedLoss.id))
                        isDeleteLossSheetOpen = false
                        selectedLoss = Loss()
                    }
                )
            }
        }
    }
}

@Composable
private fun AddLossBottomSheetContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    fieldError: LossFieldsError,
    initialLoss: Loss,
    onSaveLoss: (Loss) -> Unit,
    onDismiss: () -> Unit
) {
    var loss by remember { mutableStateOf(initialLoss) }
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.add_new_loss),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        AnimatedLoading(isLoading)
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = loss.value,
            onValueChange = {
                loss = loss.copy(value = it)
            },
            label = stringResource(R.string.value),
            withBorder = true,
            errorMessage = fieldError.message,
            isError = fieldError in listOf(
                LossFieldsError.LOSS_VALUE_IS_EMPTY,
                LossFieldsError.LOSS_VALUE_IS_INVALID
            )
        )
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = loss.reason,
            onValueChange = {
                loss = loss.copy(reason = it)
            },
            label = stringResource(R.string.reason),
            withBorder = true,
            errorMessage = fieldError.message,
            isError = fieldError in listOf(
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
                    onSaveLoss(loss)
                }
            ) {
                Text(text = stringResource(R.string.save))
            }
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
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