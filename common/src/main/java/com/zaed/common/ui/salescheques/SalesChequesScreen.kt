package com.zaed.common.ui.salescheques

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.salescheques.component.SalesChequesScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesChequesScreen(
    viewModel: SalesChequesScreenViewModel = koinViewModel(),
    navigateToAddCustomer: () -> Unit,
    onShowNavDrawer: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SalesChequesScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SalesChequesUiAction.OnDrawerClicked -> onShowNavDrawer()
                SalesChequesUiAction.OnAddNewCustomer -> navigateToAddCustomer()
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

