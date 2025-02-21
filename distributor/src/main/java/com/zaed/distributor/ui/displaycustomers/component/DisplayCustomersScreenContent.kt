package com.zaed.distributor.ui.displaycustomers.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersState
import com.zaed.distributor.ui.displaycustomers.DisplayWholeSalesCustomerUiAction
import com.zaed.distributor.ui.theme.DistributorAppTheme

@Composable
fun DisplayCustomersScreenContent(
    uiState: DisplayCustomersState = DisplayCustomersState(),
    onAction: (DisplayWholeSalesCustomerUiAction) -> Unit = {},
) {
    val selectedCustomer by remember { mutableStateOf<WholeSaleCustomer?>(null) }
    var confirmDeleteSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            AddCustomerFab(
                onClick = {
                    onAction(DisplayWholeSalesCustomerUiAction.OnAddWholeSaleCustomerClicked)
                }
            )
        },
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Box (
                modifier = Modifier.padding(12.dp)
            ){
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChanged = {
                        onAction(DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(it))
                    }

                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                itemsIndexed(uiState.displayedCustomers) { index, customer ->
                    CustomerItem(
                        customer,
                        onClick = {
                            onAction(DisplayWholeSalesCustomerUiAction.OnCustomerClicked(customer))
                        },
                        onEdit = {/*todo*/},
                        onDelete = {/*todo*/}
                    )
                }
            }
        }

        ConfirmDeleteBottomSheet(
            visible = confirmDeleteSheet,
            label = selectedCustomer?.name ?: "",
            onDismiss = {
                confirmDeleteSheet = false
            },
            onConfirm = {
                onAction(DisplayWholeSalesCustomerUiAction.OnCustomerDeleted(selectedCustomer!!))
                confirmDeleteSheet = false
            }
        )

    }
}

@Preview
@Composable
fun DisplayCustomersScreenPreview() {
    DistributorAppTheme {
        DisplayCustomersScreenContent()
    }
}