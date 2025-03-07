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
    CustomerDetailsScreenContent(
        uiState = uiState,
        selectedCustomer = uiState.customer,
        onAddPayment = {
            viewModel.addPayment(it)
        },
        onEditPayment = {},
        onAction = { action ->
            when (action) {
                CustomerDetailsUiAction.OnEditCustomer -> navigateToEditCustomer(uiState.customer.id)
                is CustomerDetailsUiAction.OnEditProductSale -> onNavigateToAddProductSale(action.saleId)
                is CustomerDetailsUiAction.OnEditGoldSale -> onNavigateToAddGoldSale(action.saleId)
                is CustomerDetailsUiAction.OnProductSaleClicked -> onNavigateToProductSaleDetails(action.saleId)
                is CustomerDetailsUiAction.OnGoldSaleClicked -> onNavigateToGoldSaleDetails(action.saleId)
                CustomerDetailsUiAction.OnBackClicked -> onBack()
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

