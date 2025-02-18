package com.zaed.distributor.ui.sales

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.sales.components.SalesList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToAddSale: (String) -> Unit,
    onNavigateToSaleDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSignedOut) {
        if(state.isSignedOut){
            onNavigateToLogin()
        }
    }
    SalesScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                SalesUiAction.AddSaleClicked -> onNavigateToAddSale("")
                is SalesUiAction.OnEditSale -> onNavigateToAddSale(action.saleId)
                is SalesUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreenContent(
    modifier: Modifier = Modifier,
    state: SalesUiState,
    onAction: (SalesUiAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title= {
                    Text(
                        text = stringResource(com.zaed.distributor.R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    MoreDropDownMenu(
                        items = listOf(
                            MoreDropdownItem(
                                onClick = { onAction(SalesUiAction.OnSignOut) },
                                title = stringResource(R.string.sign_out),
                                icon = Icons.AutoMirrored.Filled.Logout,
                                tint = MaterialTheme.colorScheme.error
                            )
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = { onAction(SalesUiAction.AddSaleClicked) },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Sale"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            //search bar
            SearchBar(
                modifier = Modifier.padding(16.dp),
                query = state.searchQuery,
                onQueryChanged = { onAction(SalesUiAction.UpdateSearchQuery(it)) }
            )
            //paid/unpaid tabs
            LazyRow {
                items(PaymentStatus.entries) { status ->
                    FilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            onAction(SalesUiAction.UpdatePaymentStatusFilter(status))
                        },
                        label = {
                            Text(stringResource(status.label))
                        },
                        selected = state.selectedPaymentStatus == status,
                        leadingIcon = {
                            if (state.selectedPaymentStatus == status) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        },
                    )
                }
            }
            //sales list
            SalesList(
                sales = state.displayedSales,
                isLoading = state.isLoading,
                onDeleteSale = { onAction(SalesUiAction.OnDeleteSale(it)) },
                onEditSale = { onAction(SalesUiAction.OnEditSale(it)) },
                onSaleClicked = { onAction(SalesUiAction.OnSaleClicked(it)) }
            )
        }
    }

}

enum class PaymentStatus(@StringRes val label: Int) {
    ALL(R.string.all),
    PAID(R.string.paid),
    UNPAID(R.string.unpaid)
}