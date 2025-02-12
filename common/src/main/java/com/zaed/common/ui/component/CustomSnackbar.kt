package com.zaed.common.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.ui.AuthenticationUiState

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    uiState: AuthenticationUiState
) {
    SnackbarHost(snackbarHostState) { data ->
        Snackbar(
            shape = RoundedCornerShape(36.dp),
            containerColor = if (uiState.errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            snackbarData = data
        )
    }
}