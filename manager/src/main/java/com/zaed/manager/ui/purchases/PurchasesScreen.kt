package com.zaed.manager.ui.purchases

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.DatedSalesWithSearchSection
import com.zaed.manager.ui.theme.ManagerTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun PurchasesScreen(
    viewModel: PurchasesViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToAddIngotPurchase: (String) -> Unit,
    onNavigateToAddGoldPurchase: (String) -> Unit,
    onNavigateToAddProductPurchase: (String) -> Unit,
    onNavigateToProductPurchaseDetails: (String) -> Unit,
    onNavigateToGoldPurchaseDetails: (String) -> Unit,
    onNavigateToGoldIngotsDetails: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PurchasesScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                PurchasesUiAction.OnShowNavDrawer -> onShowNavDrawer()
                PurchasesUiAction.AddProductPurchasesClicked -> onNavigateToAddProductPurchase("")
                PurchasesUiAction.AddGoldPurchasesClicked -> onNavigateToAddGoldPurchase("")
                is PurchasesUiAction.OnEditProductPurchases -> onNavigateToAddProductPurchase(action.purchaseId)
                is PurchasesUiAction.OnEditGoldPurchases -> onNavigateToAddGoldPurchase(action.purchaseId)
                is PurchasesUiAction.OnProductPurchasesClicked -> onNavigateToProductPurchaseDetails(action.purchaseId)
                is PurchasesUiAction.OnGoldPurchasesClicked -> onNavigateToGoldPurchaseDetails(action.purchaseId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreenContent(
    modifier: Modifier = Modifier,
    state: PurchasesUiState,
    onAction: (PurchasesUiAction) -> Unit,
) {
    var selectedTransaction by remember { mutableStateOf("" to false) }
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
                        onClick = { onAction(PurchasesUiAction.OnShowNavDrawer) }
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
                onClick = { onAction(PurchasesUiAction.AddProductPurchasesClicked) },
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
            //paid/unpaid tabs
            DatedSalesWithSearchSection(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.isLoading,
                query = state.searchQuery,
                onQueryChanged = { query ->
                    onAction(PurchasesUiAction.UpdateSearchQuery(query))
                },
                selectedFilter = state.dateFilter,
                onFilterClicked = { filter ->
                    onAction(PurchasesUiAction.UpdateDateFilter(filter))
                },
                datedSales = state.datedSales,
                onSaleClicked = { saleId , type ->
                    when(type){
                        WholesaleTransaction::class.qualifiedName -> onAction(PurchasesUiAction.OnProductPurchasesClicked(saleId))
                        else -> onAction(PurchasesUiAction.OnGoldPurchasesClicked(saleId))
                    }
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
                                if (selectedTransaction.second) {
                                    PurchasesUiAction.OnDeletePurchases(selectedTransaction.first)
                                } else {
                                    PurchasesUiAction.OnDeletePurchases(selectedTransaction.first)
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
    ManagerTheme  {
        PurchasesScreenContent(
            state = PurchasesUiState(
                currentUser = User(
                    fullName = "Mohamed Mahmoud"
                ),
                filteredPurchases = listOf(
                    WholesaleTransaction(
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
