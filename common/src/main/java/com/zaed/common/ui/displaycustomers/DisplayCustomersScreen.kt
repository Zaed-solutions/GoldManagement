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
    navigateToAddCustomer: () -> Unit,
    navigateToCustomerDetails: (String) -> Unit,
    navigateToEditCustomer:(String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisplayCustomersScreenContent(
        uiState = state,
        onAction = { action ->
            when (action) {
                DisplayWholeSalesCustomerUiAction.OnShowNavDrawer -> onShowNavDrawer()
                is DisplayWholeSalesCustomerUiAction.OnAddWholeSaleCustomerClicked -> {
                    navigateToAddCustomer()
                }
                is DisplayWholeSalesCustomerUiAction.OnEditCustomerClicked -> {
                    navigateToEditCustomer(action.customer.id)
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



