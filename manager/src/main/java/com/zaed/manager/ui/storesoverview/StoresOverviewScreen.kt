package com.zaed.manager.ui.storesoverview

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.manager.ui.storesoverview.components.SaveStoreBottomSheet
import com.zaed.manager.ui.storesoverview.components.StoreSalesContent
import com.zaed.manager.ui.storesoverview.components.StoresList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoresOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: StoresOverviewViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToStoreDetails: (String) -> Unit,
    onNavigateToSaleDetails: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StoresOverviewScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                StoresOverviewUiAction.OnBackClicked -> onNavigateBack()
                is StoresOverviewUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                is StoresOverviewUiAction.OnStoreClicked -> onNavigateToStoreDetails(action.storeId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoresOverviewScreenContent(
    modifier: Modifier = Modifier,
    state: StoresOverviewUiState,
    onAction: (StoresOverviewUiAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { StoresOverviewTab.entries.size }
    var selectedStore by remember {
        mutableStateOf(Store())
    }
    var isSaveStoreBottomSheetVisible by remember {
        mutableStateOf(false)
    }
    var isConfirmDeleteStoreSheetVisible by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.stores_overview),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(StoresOverviewUiAction.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(pagerState.currentPage == StoresOverviewTab.STORES.ordinal) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 8.dp)
                        .rotate(45f),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        selectedStore = Store()
                        isSaveStoreBottomSheetVisible = true
                    },
                ) {
                    Icon(
                        modifier = Modifier.rotate(-45f),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Store"
                    )
                }
            }
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
                StoresOverviewTab.entries.forEach { tab ->
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
                state = pagerState
            ) { currentPage ->
                when(currentPage){
                    StoresOverviewTab.STORES.ordinal -> {
                        StoresList(
                            modifier = Modifier.fillMaxSize(),
                            isLoading = state.isStoresLoading,
                            stores = state.stores,
                            onDeleteStore = {
                                selectedStore = it
                                isConfirmDeleteStoreSheetVisible = true
                            },
                            onEditStore = {
                                selectedStore = it
                                isSaveStoreBottomSheetVisible = true
                            },
                            onStoreClicked = {
                                onAction(StoresOverviewUiAction.OnStoreClicked(it.id))
                            }
                        )
                    }
                    StoresOverviewTab.SALES.ordinal -> {
                        StoreSalesContent(
                            searchQuery = state.salesSearchQuery,
                            onUpdateSearchQuery = {
                                onAction(StoresOverviewUiAction.UpdateSalesSearchQuery(it))
                            },
                            filter = state.salesFilter,
                            totalAmount = state.totalSalesAmount,
                            isLoading = state.isStoresSalesLoading,
                            filteredSales = state.filteredSales,
                            onSaleClicked = {
                                onAction(StoresOverviewUiAction.OnSaleClicked(it))
                            },
                            onUpdateFilter = {
                                onAction(StoresOverviewUiAction.UpdateSalesFilter(it))
                            },
                            categories = state.categories,
                            locations = state.locations,
                            employees = state.employees,
                            customers = state.customers
                        )
                    }
                }

            }
            SaveStoreBottomSheet(
                isVisible = isSaveStoreBottomSheetVisible,
                initialStore = selectedStore,
                onDismiss = {
                    isSaveStoreBottomSheetVisible = false
                },
                onSave = {
                    onAction(
                        if (it.id.isBlank()) {
                            StoresOverviewUiAction.OnAddStore(it)
                        } else {
                            StoresOverviewUiAction.OnUpdateStore(it)
                        }
                    )
                    isSaveStoreBottomSheetVisible = false
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteStoreSheetVisible,
                onDismiss = {
                    isConfirmDeleteStoreSheetVisible = false
                },
                onConfirm = {
                    onAction(StoresOverviewUiAction.OnDeleteStore(selectedStore))
                    isConfirmDeleteStoreSheetVisible = false
                },
                label = stringResource(R.string.store)
            )
        }
    }

}

enum class StoresOverviewTab(@StringRes val titleRes: Int) {
    STORES(R.string.stores),
    SALES(R.string.sales),
//    LOSSES(R.string.losses)
}