package com.zaed.distributor.ui.customerdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.distributor.ui.customerdetails.component.CustomerDetailsScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomerDetailsScreen(
    viewModel: CustomerDetailsViewModel = koinViewModel(),
    customerId: String
) {
    LaunchedEffect(Unit) {
        viewModel.init(customerId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CustomerDetailsScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                CustomerDetailsUiAction.OnBackClicked -> {}
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

