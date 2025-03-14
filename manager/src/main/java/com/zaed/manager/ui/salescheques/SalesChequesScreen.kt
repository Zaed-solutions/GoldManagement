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
        onShowNavDrawer = onShowNavDrawer,
        selectedCustomer = uiState.selectedCustomer,
        query = uiState.customerSearchQuery,
        onQueryChanged = viewModel::updateSearchQuery,
        suggestedCustomers = uiState.suggestedCustomers,
        onAddNewCustomer = navigateToAddCustomer,
        onCustomerSelected = viewModel::updateCustomer,
        onAddPayment =viewModel::addPayment,
        onEditPayment = viewModel::confirmEditPayment,
        isLoading = uiState.loading,
        isAdmin = uiState.isAdmin,
        onUpdateSearchQuery = viewModel::updateSupplierSearchQuery,
        searchQuery = uiState.customerSearchQuery,
        filteredSuppliers = uiState.filteredSuppliers,
        onSupplierClicked = viewModel::onSupplierClicked,
        onAddSupplier = viewModel::addSupplier,
        onAction = { action ->
            when (action) {
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

