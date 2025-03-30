package com.zaed.common.ui.displaycustomers.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.displaycustomers.DisplayCustomersState
import com.zaed.common.ui.displaycustomers.DisplayWholeSalesCustomerUiAction
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayCustomersScreenContent(
    uiState: DisplayCustomersState = DisplayCustomersState(),
    onAction: (DisplayWholeSalesCustomerUiAction) -> Unit = {},
) {
    var selectedCustomer by remember { mutableStateOf<WholeSaleCustomer?>(null) }
    var confirmDeleteSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            AddCustomerFab(
                onClick = {
                    onAction(
                        if (pagerState.currentPage == 0)
                            DisplayWholeSalesCustomerUiAction.OnAddGoldWholeSaleCustomerClicked
                        else
                            DisplayWholeSalesCustomerUiAction.OnAddSilverWholeSaleCustomerClicked
                    )
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
                onQueryChanged = { query ->
                    onAction(DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(query))
                }
            )
            if (
                uiState.currentDistributor.role == UserRole.ACCOUNTANT ||
                uiState.currentDistributor.permissions.containsAll(
                    listOf(UserPermission.SELL_SILVER, UserPermission.SELL_PRODUCTS)
                )
            ) {
                PrimaryTabRow(
                    modifier = Modifier.padding(top = 16.dp),
                    selectedTabIndex = pagerState.currentPage, indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier
                                .run {
                                    if (LocalLayoutDirection.current == LayoutDirection.Rtl) scale(
                                        -1f, 1f
                                    )
                                    else this
                                }
                                .tabIndicatorOffset(pagerState.currentPage, true),
                            width = Dp.Unspecified,
                        )
                    }
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.gold)
                            )
                        }
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.silver)
                            )
                        }
                    )
                }
                HorizontalPager(
                    modifier = Modifier.padding(top = 16.dp),
                    state = pagerState,
                    userScrollEnabled = false,
                ) { value ->
                    when (value) {
                        0 -> {
                            CustomersList(
                                customers = uiState.filteredGoldCustomers,
                                isLoading = uiState.isLoading,
                                onCustomerClicked = { customer ->
                                    onAction(
                                        DisplayWholeSalesCustomerUiAction.OnCustomerClicked(
                                            customer
                                        )
                                    )
                                },
                                onDeleteCustomer = { customer ->
                                    selectedCustomer = customer
                                    confirmDeleteSheet = true
                                },
                                onEditCustomer = { customer ->
                                    onAction(
                                        DisplayWholeSalesCustomerUiAction.OnEditGoldCustomerClicked(
                                            customer
                                        )
                                    )
                                }
                            )
                        }

                        1 -> {
                            CustomersList(
                                customers = uiState.filteredSilverCustomers,
                                isLoading = uiState.isLoading,
                                onCustomerClicked = { customer ->
                                    onAction(
                                        DisplayWholeSalesCustomerUiAction.OnCustomerClicked(
                                            customer
                                        )
                                    )
                                },
                                onDeleteCustomer = { customer ->
                                    selectedCustomer = customer
                                    confirmDeleteSheet = true
                                },
                                onEditCustomer = { customer ->
                                    onAction(
                                        DisplayWholeSalesCustomerUiAction.OnEditSilverCustomerClicked(
                                            customer
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            } else if (uiState.currentDistributor.permissions.contains(UserPermission.SELL_SILVER)) {
                CustomersList(
                    customers = uiState.filteredSilverCustomers,
                    isLoading = uiState.isLoading,
                    onCustomerClicked = { customer ->
                        onAction(DisplayWholeSalesCustomerUiAction.OnCustomerClicked(customer))
                    },
                    onDeleteCustomer = { customer ->
                        selectedCustomer = customer
                        confirmDeleteSheet = true
                    },
                    onEditCustomer = { customer ->
                        onAction(DisplayWholeSalesCustomerUiAction.OnEditSilverCustomerClicked(customer))
                    }
                )
            } else {
                CustomersList(
                    customers = uiState.filteredGoldCustomers,
                    isLoading = uiState.isLoading,
                    onCustomerClicked = { customer ->
                        onAction(DisplayWholeSalesCustomerUiAction.OnCustomerClicked(customer))
                    },
                    onDeleteCustomer = { customer ->
                        selectedCustomer = customer
                        confirmDeleteSheet = true
                    },
                    onEditCustomer = { customer ->
                        onAction(DisplayWholeSalesCustomerUiAction.OnEditGoldCustomerClicked(customer))
                    }
                )
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
                confirmDeleteSheet = false
                onAction(DisplayWholeSalesCustomerUiAction.OnCustomerDeleted(selectedCustomer!!))
                selectedCustomer = null
            }
        )
    }
}

@Composable
fun CustomersList(
    modifier: Modifier = Modifier,
    customers: List<WholeSaleCustomer>,
    isLoading: Boolean,
    onCustomerClicked: (WholeSaleCustomer) -> Unit,
    onEditCustomer: (WholeSaleCustomer) -> Unit,
    onDeleteCustomer: (WholeSaleCustomer) -> Unit,
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = customers,
                key = { customer -> customer.id }
            ) { customer ->
                CustomerItem(
                    modifier = Modifier.animateItem(),
                    customer = customer,
                    onClick = {
                        onCustomerClicked(customer)
                    },
                    onEdit = {
                        onEditCustomer(customer)
                    },
                    onDelete = {
                        onDeleteCustomer(customer)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DisplayCustomersScreenPreview() {
    DisplayCustomersScreenContent()
}