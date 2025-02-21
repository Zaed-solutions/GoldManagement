package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zaed.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ConfirmDeleteBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    label: String = "",
    subtitle: String = stringResource(R. string. this_action_cannot_be_undone),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            ConfirmDeleteDialog(
                modifier = modifier,
                label = label,
                subtitle = subtitle,
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }
    }
}