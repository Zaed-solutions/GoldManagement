package com.zaed.manager.ui.distributorssales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.PriceCalculationItem
import com.zaed.common.ui.components.SearchBar
import com.zaed.manager.ui.distributorssales.components.DistributorSalesFilterBottomSheet
import com.zaed.manager.ui.distributorssales.components.DistributorsSalesList
import com.zaed.manager.ui.storessales.components.StoreSalesFilterBottomSheet
import com.zaed.manager.ui.storessales.components.StoresSalesList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DistributorsSalesScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    onNavigateToSaleDetails: (String) -> Unit,
    viewModel: DistributorsSalesViewModel = koinViewModel()
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DistributorsSalesScreenContent(
        state = state,
        onAction = {action->
            when(action){
                is DistributorsSalesUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                DistributorsSalesUiAction.OnShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorsSalesScreenContent(
    modifier: Modifier = Modifier,
    state: DistributorsSalesUiState,
    onAction: (DistributorsSalesUiAction) -> Unit
){
    var isFilterSheetVisible by remember{
        mutableStateOf(false)
    }
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.distributors_sales),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(DistributorsSalesUiAction.OnShowNavDrawer)
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
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            //search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    placeHolder = stringResource(R.string.search_by_receipt_number),
                    query = state.searchQuery,
                    onQueryChanged = {
                        onAction(DistributorsSalesUiAction.UpdateSearchQuery(it))
                    }
                )
                BadgedBox(
                    badge = {
                        if(state.filter.isFiltered) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                ) {
                    IconButton(
                        onClick = {
                            isFilterSheetVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    shape = CircleShape
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
            PriceCalculationItem(
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp),
                title = stringResource(R.string.total_amount),
                price = state.totalAmount,
                style = MaterialTheme.typography.titleMedium
            )
            //stores sales
            DistributorsSalesList(
                isLoading = state.isLoading,
                sales = state.filteredSales,
                onSaleClicked = {
                    onAction(DistributorsSalesUiAction.OnSaleClicked(it.id))
                }
            )
            //filter bottom sheet
            DistributorSalesFilterBottomSheet(
                isVisible = isFilterSheetVisible,
                onDismiss = {
                    isFilterSheetVisible = false
                },
                onSubmitFilter = {
                    onAction(DistributorsSalesUiAction.UpdateFilter(it))
                    isFilterSheetVisible = false
                },
                initialFilter = state.filter,
                categories = state.categories,
                locations = state.locations,
                employees = state.employees,
                customers = state.customers
            )
        }
    }
}