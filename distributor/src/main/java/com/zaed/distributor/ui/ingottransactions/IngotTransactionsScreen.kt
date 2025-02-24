package com.zaed.distributor.ui.ingottransactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.distributor.ui.ingottransactions.components.SaveIngotTransactionBottomSheet
import com.zaed.distributor.ui.ingottransactions.components.TransactionsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun IngotTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: IngotTransactionsViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    IngotTransactionsScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                IngotTransactionsUiAction.OnShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngotTransactionsScreenContent(
    modifier: Modifier = Modifier,
    state: IngotTransactionsUiState,
    onAction: (IngotTransactionsUiAction) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    var isSaveTransactionSheetVisible by remember {
        mutableStateOf(false)
    }
    var isConfirmDeleteSheetVisible by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    var selectedTransaction by remember {
        mutableStateOf(IngotTransaction())
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.ingots),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(IngotTransactionsUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedTransaction = IngotTransaction()
                    isSaveTransactionSheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            //search bar
            SearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                query = state.searchQuery,
                onQueryChanged = {
                    onAction(IngotTransactionsUiAction.OnUpdateSearchQuery(it))
                }
            )
            //tab row
            PrimaryTabRow(
                modifier = Modifier.padding(top = 16.dp),
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .run {
                                if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                                    scale(-1f, 1f)
                                else
                                    this
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
                            text = stringResource(R.string.sales),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                            text = stringResource(R.string.purchases),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            //horizontal pager
            HorizontalPager(
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> {
                        //sales list
                        TransactionsList(
                            isLoading = state.isLoading,
                            transactions = state.displayedSaleTransactions,
                            onEditTransaction = {
                                selectedTransaction = it
                                isSaveTransactionSheetVisible = true
                            },
                            onDeleteTransaction = {
                                selectedTransaction = it
                                isConfirmDeleteSheetVisible = true
                            }
                        )
                    }

                    1 -> {
                        //purchases list
                        TransactionsList(
                            isLoading = state.isLoading,
                            transactions = state.displayedPurchaseTransactions,
                            onEditTransaction = {
                                selectedTransaction = it
                                isSaveTransactionSheetVisible = true
                            },
                            onDeleteTransaction = {
                                selectedTransaction = it
                                isConfirmDeleteSheetVisible = true
                            }
                        )
                    }
                }
            }
            SaveIngotTransactionBottomSheet(
                isVisible = isSaveTransactionSheetVisible,
                initialTransaction = selectedTransaction,
                onDismiss = {
                    isSaveTransactionSheetVisible = false
                },
                onSave = { transaction ->
                    onAction(IngotTransactionsUiAction.OnSaveTransaction(transaction))
                    isSaveTransactionSheetVisible = false
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteSheetVisible,
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    onAction(IngotTransactionsUiAction.OnDeleteTransaction(selectedTransaction))
                    isConfirmDeleteSheetVisible = false
                },
                label = stringResource(R.string.transaction)
            )
        }
    }
}