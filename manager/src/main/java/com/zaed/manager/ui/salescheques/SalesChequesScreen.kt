package com.zaed.manager.ui.salescheques

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.manager.ui.salescheques.component.SalesChequesScreenContent
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
        selectedCustomer = uiState.selectedCustomer,
        query = uiState.customerSearchQuery,
        suggestedCustomers = uiState.suggestedCustomers,
        isLoading = uiState.loading,
        isAdmin = uiState.isAdmin,
        searchQuery = uiState.customerSearchQuery,
        filteredSuppliers = uiState.filteredSuppliers,
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

