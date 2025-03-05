package com.zaed.manager.ui.storedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import org.koin.androidx.compose.koinViewModel
import com.zaed.common.R
import com.zaed.common.ui.components.DatedListWithFilter
import com.zaed.common.ui.components.DatedLossesList
import com.zaed.common.ui.components.DatedSalesWithSearchSection
import com.zaed.common.ui.components.StoreInventorySection
import com.zaed.manager.ui.stores.components.SaveStoreBottomSheet
import kotlinx.coroutines.launch

@Composable
fun StoreDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreDetailsViewModel = koinViewModel(),
    storeId: String,
    onBackClicked: () -> Unit,
    onNavigateToSaleDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (true){
        viewModel.init(storeId)
    }
    StoreDetailsScreenContent(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when(action){
                StoreDetailsUiAction.OnBackClicked -> onBackClicked()
                is StoreDetailsUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.id)
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: StoreDetailsUiState,
    onAction: (StoreDetailsUiAction) -> Unit
) {
    val pagerState = rememberPagerState { StoreDetailsTab.entries.size }
    val scope = rememberCoroutineScope()
    var isEditStoreSheetVisible by remember {
        mutableStateOf(false)
    }
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = state.store.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = state.store.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(StoreDetailsUiAction.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    MoreDropDownMenu(
                        items = listOf(
                            MoreDropdownItem(
                                title = stringResource(id = R.string.edit),
                                icon = Icons.Default.Edit,
                                onClick = {
                                    isEditStoreSheetVisible = true
                                },
                                tint = MaterialTheme.colorScheme.onSurface
                            ),
                            MoreDropdownItem(
                                title = stringResource(id = R.string.delete),
                                icon = Icons.Default.Delete,
                                onClick = {
                                    onAction(StoreDetailsUiAction.OnDeleteStore)
                                },
                                tint = MaterialTheme.colorScheme.error
                            )
                        )
                    )
                }
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
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
                StoreDetailsTab.entries.forEach { tab ->
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
                state = pagerState,
            ) { page ->
                when(page){
                    StoreDetailsTab.SALES.ordinal -> {
                        DatedSalesWithSearchSection(
                            modifier = Modifier.fillMaxSize(),
                            isLoading = state.isLoading,
                            query = state.salesQuery,
                            onQueryChanged = { query ->
                                onAction(StoreDetailsUiAction.OnSalesQueryChanged(query))
                            },
                            selectedFilter = state.selectedSalesFilter,
                            onFilterClicked = { filter ->
                                onAction(StoreDetailsUiAction.UpdateSalesDateFilter(filter))
                            },
                            datedSales = state.displayedDatedSales,
                            onSaleClicked = { saleId ->
                                onAction(StoreDetailsUiAction.OnSaleClicked(saleId))
                            }
                        )
                    }
                    StoreDetailsTab.INVENTORY.ordinal -> {
                        StoreInventorySection(
                            modifier = Modifier.fillMaxSize(),
                            isLoading = state.isLoading,
                            query = state.inventoryQuery,
                            onQueryChanged = { query ->
                                onAction(StoreDetailsUiAction.OnInventoryQueryChanged(query))
                            },
                            inventories = state.displayedInventories,
                            onAddInventory = {
                                TODO()
                            }
                        )
                    }
                    StoreDetailsTab.LOSSES.ordinal -> {
                        DatedListWithFilter(
                            isLoading = state.isLoading,
                            selectedFilter = state.selectedLossesFilter,
                            onFilterClicked = { onAction(StoreDetailsUiAction.UpdateLossesDateFilter(it)) },
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
            SaveStoreBottomSheet(
                isVisible = isEditStoreSheetVisible,
                onDismiss = {
                    isEditStoreSheetVisible = false
                },
                onSave = {
                    isEditStoreSheetVisible = false
                    onAction(StoreDetailsUiAction.OnUpdateStore(it))
                },
                initialStore = state.store
            )
        }
    }

}

enum class StoreDetailsTab(val titleRes: Int) {
    SALES(R.string.sales),
    INVENTORY(R.string.inventory),
    LOSSES(R.string.losses)
}