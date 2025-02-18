package com.zaed.distributor.ui.displaycustomers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.theme.DistributorAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DisplayCustomersScreen(
    viewModel: DisplayCustomersViewModel = koinViewModel(),
    navigateToAddCustomer: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisplayCustomersScreenContent(
        uiState = state,
        onAction = { action ->
            when (action) {
                is DisplayWholeSalesCustomerUiAction.OnAddWholeSaleCustomerClicked -> {
                    navigateToAddCustomer()
                }

                else -> {
                    viewModel.handleAction(action)
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayCustomersScreenContent(
    uiState: DisplayCustomersState = DisplayCustomersState(),
    onAction: (DisplayWholeSalesCustomerUiAction) -> Unit = {}
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                var expanded by remember { mutableStateOf(false) }
                DockedSearchBar(
                    modifier = Modifier.padding(8.dp),
                    expanded = expanded,
                    shape = RoundedCornerShape(8.dp),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = uiState.searchQuery,
                            onSearch = {
                                expanded = false
                                onAction(
                                    DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(
                                        it
                                    )
                                )
                            },
                            expanded = expanded,
                            onQueryChange = {
                                onAction(
                                    DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(
                                        it
                                    )
                                )
                            },
                            onExpandedChange = { expanded = it },
                            placeholder = { Text("Search here...") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty() || expanded) {
                                    IconButton(
                                        onClick = {
                                            onAction(
                                                DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged(
                                                    ""
                                                )
                                            )
                                            expanded = false
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Clear,
                                            contentDescription = "Clear Icon",
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    onExpandedChange = { expanded = it }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentHeight()
                    ) {
                        itemsIndexed(uiState.displayedCustomers) { index, customer ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                shape = RoundedCornerShape(8.dp),
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(customer.phone)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(customer.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                itemsIndexed(uiState.customers) { index, customer ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            Row {
                                Text(
                                    text = customer.name,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = customer.phone,
                                )
                            }
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                            Row {
                                Text(
                                    text = customer.debtAmount.toMoneyFormat(2),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                FilterChip(
                                    modifier = Modifier.height(FilterChipDefaults.Height - 4.dp),
                                    selected = true,
                                    onClick = { /*TODO*/ },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = if (customer.inDebt) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                    label = {
                                        Text(
                                            text = if (customer.inDebt) "In Debt" else "Not In Debt"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerFab(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.rotate(45f),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.rotate(-45f),
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Customer"
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

