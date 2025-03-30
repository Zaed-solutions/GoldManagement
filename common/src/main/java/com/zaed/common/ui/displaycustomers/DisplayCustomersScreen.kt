package com.zaed.common.ui.displaycustomers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.displaycustomers.component.DisplayCustomersScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun DisplayCustomersScreen(
    viewModel: DisplayCustomersViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    navigateToAddGoldCustomer: (customerId: String) -> Unit,
    navigateToAddSilverCustomer: (customerId: String) -> Unit,
    navigateToCustomerDetails: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisplayCustomersScreenContent(
        uiState = state,
        onAction = { action ->
            when (action) {
                DisplayWholeSalesCustomerUiAction.OnShowNavDrawer -> onShowNavDrawer()
                is DisplayWholeSalesCustomerUiAction.OnAddGoldWholeSaleCustomerClicked -> {
                    navigateToAddGoldCustomer("")
                }
                is DisplayWholeSalesCustomerUiAction.OnEditGoldCustomerClicked -> {
                    navigateToAddGoldCustomer(action.customer.id)
                }
                DisplayWholeSalesCustomerUiAction.OnAddSilverWholeSaleCustomerClicked -> {
                    navigateToAddSilverCustomer("")
                }
                is DisplayWholeSalesCustomerUiAction.OnEditSilverCustomerClicked -> {
                    navigateToAddSilverCustomer(action.customer.id)
                }
                is DisplayWholeSalesCustomerUiAction.OnCustomerClicked -> {
                    navigateToCustomerDetails(action.customer.id)
                }
                else -> {
                    viewModel.handleAction(action)
                }
            }
        }
    )

}



