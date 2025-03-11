package com.zaed.manager.ui.salescheques

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.manager.ui.salescheques.component.SalesChequesScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesChequesScreen(
    viewModel: SalesChequesScreenViewModel = koinViewModel(),
    customerId: String,
    navigateToEditCustomer: (String) -> Unit,
    onNavigateToProductSaleDetails: (String) -> Unit,
    onNavigateToGoldSaleDetails: (String) -> Unit,
    onNavigateToAddProductSale: (String) -> Unit,
    onNavigateToAddGoldSale: (String) -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.init(customerId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SalesChequesScreenContent(
        uiState = uiState,
        selectedCustomer = uiState.customer,
        onAddPayment = {
            viewModel.addPayment(it)
        },
        onEditPayment = viewModel::confirmEditPayment,
        onAction = { action ->
            when (action) {
                SalesChequesUiAction.OnEditCustomer -> navigateToEditCustomer(uiState.customer.id)
                is SalesChequesUiAction.OnEditProductSale -> onNavigateToAddProductSale(action.saleId)
                is SalesChequesUiAction.OnEditGoldSale -> onNavigateToAddGoldSale(action.saleId)
                is SalesChequesUiAction.OnProductSaleClicked -> onNavigateToProductSaleDetails(action.saleId)
                is SalesChequesUiAction.OnGoldSaleClicked -> onNavigateToGoldSaleDetails(action.saleId)
                SalesChequesUiAction.OnBackClicked -> onBack()
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

