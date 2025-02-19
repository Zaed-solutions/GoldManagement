package com.zaed.distributor.ui.displaycustomers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.distributor.ui.displaycustomers.component.DisplayCustomersScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun DisplayCustomersScreen(
    viewModel: DisplayCustomersViewModel = koinViewModel(),
    navigateToAddCustomer: () -> Unit,
    navigateToCustomerDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisplayCustomersScreenContent(
        uiState = state,
        onAction = { action ->
            when (action) {
                is DisplayWholeSalesCustomerUiAction.OnAddWholeSaleCustomerClicked -> {
                    navigateToAddCustomer()
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



