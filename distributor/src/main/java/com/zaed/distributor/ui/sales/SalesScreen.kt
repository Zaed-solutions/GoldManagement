package com.zaed.distributor.ui.sales

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.GridGoldenratio
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.authentication.Permission
import com.zaed.common.ui.components.FabItem
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.components.MultiFloatingActionButton
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.sales.components.SalesList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesScreen(
    viewModel: SalesViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToAddProductSale: (String) -> Unit,
    onNavigateToAddGoldSale: (String) -> Unit,
    onNavigateToProductSaleDetails: (String) -> Unit,
    onNavigateToGoldSaleDetails: (String) -> Unit
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
                SalesUiAction.AddProductSaleClicked -> onNavigateToAddProductSale("")
                SalesUiAction.AddGoldSaleClicked -> onNavigateToAddGoldSale("")
                is SalesUiAction.OnEditProductSale -> onNavigateToProductSaleDetails(action.saleId)
                is SalesUiAction.OnEditGoldSale -> onNavigateToGoldSaleDetails(action.saleId)
                is SalesUiAction.OnProductSaleClicked -> onNavigateToProductSaleDetails(action.saleId)
                is SalesUiAction.OnGoldSaleClicked -> onNavigateToGoldSaleDetails(action.saleId)
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
    context: Context = LocalContext.current
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
            if(state.currentUser.permissions.contains(Permission.SELL_GOLD)){
                val fabItems = remember {
                    listOf(
                        FabItem(
                            icon = Icons.Default.GridGoldenratio,
                            label = context.getString(R.string.add_sale),
                            onFabItemClicked = {
                                onAction(SalesUiAction.AddProductSaleClicked)
                            }
                        )
                    )
                }
                MultiFloatingActionButton(
                    fabIcon = Icons.Default.Add,
                    items = emptyList(),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            } else {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).rotate(45f),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { onAction(SalesUiAction.AddProductSaleClicked) },
                ) {
                    Icon(
                        modifier = Modifier.rotate(-45f),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Sale"
                    )
                }
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
            LazyRow (
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ){
                items(PaymentStatus.entries) { status ->
                    FilterChip(
                        onClick = {
                            onAction(SalesUiAction.UpdatePaymentStatusFilter(status))
                        },
                        label = {
                            Text(stringResource(status.label))
                        },
                        shape = MaterialTheme.shapes.large,
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
                onDeleteSale = { saleId, isProduct ->
                    onAction(
                        if(isProduct){
                            SalesUiAction.OnDeleteProductSale(saleId)
                        } else {
                            SalesUiAction.OnDeleteGoldSale(saleId)
                        }
                    )
                               },
                onEditSale = { saleId, isProduct ->
                    onAction(
                        if(isProduct){
                            SalesUiAction.OnEditProductSale(saleId)
                        } else {
                            SalesUiAction.OnEditGoldSale(saleId)
                        }
                    )
                },
                onSaleClicked = { saleId, isProduct ->
                    onAction(
                        if(isProduct){
                            SalesUiAction.OnProductSaleClicked(saleId)
                        } else {
                            SalesUiAction.OnGoldSaleClicked(saleId)
                        }
                    )
                }
            )
        }
    }

}

enum class PaymentStatus(@StringRes val label: Int) {
    ALL(R.string.all),
    PAID(R.string.paid),
    UNPAID(R.string.unpaid)
}