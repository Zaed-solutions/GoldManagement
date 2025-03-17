package com.zaed.manager.ui.distributordetails

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
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
import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.sale.WholesaleProductTransaction
import com.zaed.common.ui.components.DatedIngotTransactionsList
import com.zaed.common.ui.components.DatedListWithFilter
import com.zaed.common.ui.components.DatedLossesList
import com.zaed.common.ui.components.DatedSalesWithSearchSection
import com.zaed.common.ui.components.StoreInventorySection
import com.zaed.manager.ui.storedetails.components.SaveInventoryBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DistributorDetailsScreen(
    modifier: Modifier = Modifier,
    distributorId: String,
    onBackPressed: () -> Unit,
    onNavigateToProductSaleDetails: (String) -> Unit,
    onNavigateToGoldSaleDetails: (String) -> Unit,
    viewModel: DistributorDetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (true){
        viewModel.init(distributorId)
    }
    DistributorScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                DistributorDetailsUiAction.OnBackClicked -> onBackPressed()
                is DistributorDetailsUiAction.OnSaleClicked -> {
                    if(action.type == WholesaleProductTransaction::class.qualifiedName){
                        onNavigateToProductSaleDetails(action.saleId)
                    } else {
                        onNavigateToGoldSaleDetails(action.saleId)
                    }
                }
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorScreenContent(
    modifier: Modifier = Modifier,
    state: DistributorDetailsUiState,
    onAction: (DistributorDetailsUiAction) -> Unit
) {
    val pagerState =
        rememberPagerState { if (state.distributor.permissions.contains(UserPermission.SELL_INGOTS)) 4 else 3 }
    val scope = rememberCoroutineScope()
    var selectedInventory by remember { mutableStateOf(Inventory()) }
    var isSaveInventoryBottomSheetVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.distributor.fullName,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(DistributorDetailsUiAction.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

            PrimaryTabRow(
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
                DistributorDetailsTab.entries.take(pagerState.pageCount).forEach { tab ->
                    Tab(
                        selected = pagerState.currentPage == tab.ordinal,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(tab.ordinal)
                            }
                        },
                        text = {
                            Text(text = stringResource(tab.titleRes))
                        }
                    )
                }
            }
            HorizontalPager(
                modifier = Modifier.padding(top = 16.dp),
                state = pagerState,
            ) { page ->
                when (page) {
                    DistributorDetailsTab.SALES.ordinal -> {
                        DatedSalesWithSearchSection(
                            onCustomRangeSelected = {TODO()},
                            modifier = Modifier.fillMaxSize(),
                            isLoading = state.isLoading,
                            query = state.salesQuery,
                            onQueryChanged = { query ->
                                onAction(DistributorDetailsUiAction.OnSalesQueryChanged(query))
                            },
                            selectedFilter = state.selectedSalesFilter,
                            onFilterClicked = { filter ->
                                onAction(DistributorDetailsUiAction.UpdateSalesDateFilter(filter))
                            },
                            datedSales = state.datedSales,
                            onSaleClicked = { saleId , type ->
                                onAction(DistributorDetailsUiAction.OnSaleClicked(saleId, type))
                            }
                        )
                    }

                    DistributorDetailsTab.INGOTS.ordinal -> {
                        DatedListWithFilter(
                            selectedFilter = state.ingotTransactionsDateFormat,
                            onFilterClicked = {
                                onAction(
                                    DistributorDetailsUiAction.UpdateIngotTransactionsDateFilter(
                                        it
                                    )
                                )
                            },
                            onCustomRangeSelected = {TODO()},
                            content = {
                                DatedIngotTransactionsList(
                                    isLoading = state.isLoading,
                                    datedTransactions = state.datedIngotTransactions,
                                )
                            }
                        )
                    }

                    DistributorDetailsTab.INVENTORY.ordinal -> {
                        StoreInventorySection(
                            modifier = Modifier
                                .fillMaxSize(),
                            isLoading = state.isLoading,
                            query = state.inventoryQuery,
                            onQueryChanged = { query ->
                                onAction(DistributorDetailsUiAction.OnInventoryQueryChanged(query))
                            },
                            inventories = state.displayedInventories,
                            onAddInventory = {
                                selectedInventory = Inventory()
                                isSaveInventoryBottomSheetVisible = true
                            },
                            onInventoryClicked = { inventory ->
                                selectedInventory = inventory.copy(quantity = 0.0)
                                isSaveInventoryBottomSheetVisible = true
                            }
                        )
                    }

                    DistributorDetailsTab.LOSSES.ordinal -> {
                        DatedListWithFilter(
                            selectedFilter = state.selectedLossesFilter,
                            onFilterClicked = {
                                onAction(
                                    DistributorDetailsUiAction.UpdateLossesDateFilter(
                                        it
                                    )
                                )
                            },
                            onCustomRangeSelected = {TODO()},
                            content = {
                                DatedLossesList(
                                    isLoading = state.isLoading,
                                    datedLosses = state.datedLosses,
                                )
                            }
                        )
                    }
                }
            }
            SaveInventoryBottomSheet(
                isVisible = isSaveInventoryBottomSheetVisible,
                onDismiss = {
                    isSaveInventoryBottomSheetVisible = false
                },
                initialInventory = selectedInventory,
                inventories = state.mainInventories,
                onSaveInventory = {
                    isSaveInventoryBottomSheetVisible = false
                    onAction(DistributorDetailsUiAction.OnSaveInventory(it))
                }
            )
        }
    }
}

enum class DistributorDetailsTab(
    @StringRes val titleRes: Int
) {
    SALES(R.string.sales),
    INVENTORY(R.string.inventory),
    LOSSES(R.string.losses),
    INGOTS(R.string.ingots)
}