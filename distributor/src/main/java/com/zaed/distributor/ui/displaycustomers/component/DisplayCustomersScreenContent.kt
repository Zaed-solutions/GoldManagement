package com.zaed.distributor.ui.displaycustomers.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersState
import com.zaed.distributor.ui.displaycustomers.DisplayWholeSalesCustomerUiAction
import com.zaed.distributor.ui.sales.SalesUiAction
import com.zaed.distributor.ui.theme.DistributorAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayCustomersScreenContent(
    uiState: DisplayCustomersState = DisplayCustomersState(),
    onAction: (DisplayWholeSalesCustomerUiAction) -> Unit = {},
) {
    var selectedCustomer by remember { mutableStateOf<WholeSaleCustomer?>(null) }
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
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.customers),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(DisplayWholeSalesCustomerUiAction.OnShowNavDrawer) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu icon"
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = uiState.searchQuery,
                onQueryChanged = {
                    onAction(DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(it))
                }

            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                itemsIndexed(uiState.displayedCustomers) { index, customer ->
                    CustomerItem(
                        customer,
                        onClick = {
                            onAction(DisplayWholeSalesCustomerUiAction.OnCustomerClicked(customer))
                        },
                        onEdit = {
                            onAction(
                                DisplayWholeSalesCustomerUiAction.OnEditCustomerClicked(
                                    customer
                                )
                            )
                        },
                        onDelete = {
                            selectedCustomer = customer
                            confirmDeleteSheet = true
                        }
                    )
                }
            }
        }
        ConfirmDeleteBottomSheet(
            visible = confirmDeleteSheet,
            label = selectedCustomer?.name ?: "",
            subtitle = stringResource(R.string.all_transaction_and_payments_will_be_deleted),
            onDismiss = {
                selectedCustomer = null
                confirmDeleteSheet = false
            },
            onConfirm = {
                onAction(DisplayWholeSalesCustomerUiAction.OnCustomerDeleted(selectedCustomer!!))
                selectedCustomer = null
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