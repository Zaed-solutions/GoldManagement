package com.zaed.distributor.ui.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.sales.components.SalesList
import com.zaed.distributor.ui.theme.DistributorAppTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun SalesScreen(
    viewModel: SalesViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAddProductSale: (String) -> Unit,
    onNavigateToAddGoldSale: (String) -> Unit,
    onNavigateToProductSaleDetails: (String) -> Unit,
    onNavigateToGoldSaleDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSignedOut) {
        if (state.isSignedOut) {
            onNavigateToLogin()
        }
    }
    SalesScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                SalesUiAction.OnShowNavDrawer -> onShowNavDrawer()
                SalesUiAction.AddProductSaleClicked -> onNavigateToAddProductSale("")
                SalesUiAction.AddGoldSaleClicked -> onNavigateToAddGoldSale("")
                is SalesUiAction.OnEditProductSale -> onNavigateToAddProductSale(action.saleId)
                is SalesUiAction.OnEditGoldSale -> onNavigateToAddGoldSale(action.saleId)
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
) {

    var selectedSale by remember { mutableStateOf("" to false) }
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.currentUser.fullName,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SalesUiAction.OnShowNavDrawer) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu icon"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(),
                shape = CircleShape,
                onClick = { onAction(SalesUiAction.AddProductSaleClicked) },
            ) {
                Text(text = "New Sale")
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Default.ShoppingBag,
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                query = state.searchQuery,
                onQueryChanged = { onAction(SalesUiAction.UpdateSearchQuery(it)) }
            )
            //paid/unpaid tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
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
                    selectedSale = saleId to isProduct
                    isConfirmDeleteVisible = true
                },
                onEditSale = { saleId, isProduct ->
                    onAction(
                        if (isProduct) {
                            SalesUiAction.OnEditProductSale(saleId)
                        } else {
                            SalesUiAction.OnEditGoldSale(saleId)
                        }
                    )
                },
                onSaleClicked = { saleId, isProduct ->
                    onAction(
                        if (isProduct) {
                            SalesUiAction.OnProductSaleClicked(saleId)
                        } else {
                            SalesUiAction.OnGoldSaleClicked(saleId)
                        }
                    )
                }
            )
            AnimatedVisibility(isConfirmDeleteVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isConfirmDeleteVisible = false
                    }
                ) {
                    ConfirmDeleteDialog(
                        label = stringResource(R.string.sale),
                        onDismiss = {
                            isConfirmDeleteVisible = false
                        },
                        onConfirm = {
                            onAction(
                                if (selectedSale.second) {
                                    SalesUiAction.OnDeleteProductSale(selectedSale.first)
                                } else {
                                    SalesUiAction.OnDeleteGoldSale(selectedSale.first)
                                }
                            )
                            isConfirmDeleteVisible = false
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun SalesScreenContentPreview() {
    DistributorAppTheme {
        SalesScreenContent(
            state = SalesUiState(
                currentUser = User(
                    fullName = "Mohamed Mahmoud"
                ),
                displayedSales = listOf(
                    WholesaleProductSale(
                        createdAt = Date(),
                        customerName = "Ali",
                        products = listOf(
                            Product(
                                name = "Gold",
                                quantity = 10,
                                gramPrice = 100.0,
                                grams = 10.0
                            )
                        )
                    )
                ),
            ),
            onAction = {}
        )
    }
    
}
