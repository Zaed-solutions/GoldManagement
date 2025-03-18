package com.zaed.manager.ui.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.components.TransactionsList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    onNavigateToEditPurchase: (String) -> Unit,
    onNavigateToEditSale: (String) -> Unit,
    onNavigateToPurchaseDetails: (String) -> Unit,
    onNavigateToSaleDetails: (String) -> Unit,
    viewModel: TransactionsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsScreenContent(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when(action){
                is TransactionsUiAction.OnEditPurchaseClicked -> onNavigateToEditPurchase(action.purchaseId)
                is TransactionsUiAction.OnEditSaleClicked -> onNavigateToEditSale(action.saleId)
                is TransactionsUiAction.OnPurchaseClicked -> onNavigateToPurchaseDetails(action.purchaseId)
                is TransactionsUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                TransactionsUiAction.OnShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsScreenContent(
    modifier: Modifier = Modifier,
    state: TransactionsUiState,
    onAction: (TransactionsUiAction) -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    var isSale by remember {
        mutableStateOf(true)
    }
    var selectedTransactionId: String by remember {
        mutableStateOf("")
    }
    var isConfirmDeleteSheetVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.transactions),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(TransactionsUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = state.searchQuery,
                onQueryChanged = {
                    onAction(TransactionsUiAction.OnSearchQueryChanged(it))
                }
            )
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage, indicator = {
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
            }) {
                TransactionsTabs.entries.forEach { tab ->
                    Tab(
                        selected = pagerState.currentPage == tab.ordinal,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(tab.ordinal)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(tab.titleRes)
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { value ->
                when (value) {
                    TransactionsTabs.SALES.ordinal -> {
                        TransactionsList(
                            isLoading = state.isLoading,
                            transactions = state.displayedSales,
                            onTransactionClicked = { saleId, isProductSale ->
                                onAction(TransactionsUiAction.OnSaleClicked(saleId))
                            },
                            onEditTransaction = { saleId, isProductSale ->
                                onAction(TransactionsUiAction.OnEditSaleClicked(saleId))
                            },
                            onDeleteTransaction = { saleId, isProductSale ->
                                selectedTransactionId = saleId
                                isSale = true
                                isConfirmDeleteSheetVisible = true
                            },
                        )
                    }

                    TransactionsTabs.PURCHASES.ordinal -> {
                        TransactionsList(
                            isLoading = state.isLoading,
                            transactions = state.displayedPurchases,
                            onTransactionClicked = { purchaseId, _ ->
                                onAction(TransactionsUiAction.OnPurchaseClicked(purchaseId))
                            },
                            onEditTransaction = { purchaseId, _ ->
                                onAction(TransactionsUiAction.OnEditPurchaseClicked(purchaseId))
                            },
                            onDeleteTransaction = { purchaseId, _ ->
                                selectedTransactionId = purchaseId
                                isSale = false
                                isConfirmDeleteSheetVisible = true
                            },
                        )
                    }

                }
            }
            ConfirmDeleteBottomSheet(
                label = stringResource(
                    if (isSale) {
                        R.string.sale
                    } else {
                        R.string.purchase
                    }
                ),
                visible = isConfirmDeleteSheetVisible,
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    isConfirmDeleteSheetVisible = false
                    onAction(
                        if (isSale) {
                            TransactionsUiAction.OnDeleteSale(selectedTransactionId)
                        } else {
                            TransactionsUiAction.OnDeletePurchase(selectedTransactionId)
                        }
                    )
                }
            )
        }
    }
}

private enum class TransactionsTabs(val titleRes: Int) {
    SALES(R.string.sales),
    PURCHASES(R.string.purchases)
}