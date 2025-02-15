package com.zaed.cashier.ui.loss.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AnimatedBottomSheetWithCreateAndDismiss(
    isCreateNewLossSheetOpen: Boolean,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(isCreateNewLossSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                onDismiss()
            },
        ) {
            content()
        }
    }
}