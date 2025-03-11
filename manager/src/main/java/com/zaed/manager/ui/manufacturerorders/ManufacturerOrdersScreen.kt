package com.zaed.manager.ui.manufacturerorders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBarWithFilterButton
import com.zaed.manager.ui.manufacturerorders.components.ManufacturerOrdersFilterBottomSheet
import com.zaed.manager.ui.manufacturerorders.components.ManufacturerOrdersList
import com.zaed.manager.ui.manufacturerorders.components.SaveManufacturerOrderBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManufacturerOrdersScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    viewModel: ManufacturerOrdersViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ManufacturerOrdersScreenContent(
        state = state,
        onAction = {action ->
            when(action){
                ManufacturerOrdersUiAction.ShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturerOrdersScreenContent(
    modifier: Modifier = Modifier,
    state: ManufacturerOrdersUiState,
    onAction: (ManufacturerOrdersUiAction) -> Unit
) {
    var selectedOrder by remember{
        mutableStateOf(ManufacturerOrder())
    }
    var isConfirmDeleteVisible by remember{
        mutableStateOf(false)
    }
    var isSaveOrderSheetVisible by remember{
        mutableStateOf(false)
    }
    var isFilterSheetVisible by remember{
        mutableStateOf(false)
    }
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manufacturer_orders),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(ManufacturerOrdersUiAction.ShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedOrder = ManufacturerOrder()
                    isSaveOrderSheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Loss"
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBarWithFilterButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                searchQuery = state.searchQuery,
                onQueryChanged = {
                    onAction(ManufacturerOrdersUiAction.UpdateSearchQuery(it))
                },
                isFiltered = state.filter.isFiltered,
                onFilterClicked = {
                    isFilterSheetVisible = true
                }
            )
            ManufacturerOrdersList(
                isLoading = state.isLoading,
                orders = state.displayedOrders,
                onEdit = {
                    selectedOrder = it
                    isSaveOrderSheetVisible = true
                },
                onDelete = {
                    selectedOrder = it
                    isConfirmDeleteVisible = true
                }
            )
            ManufacturerOrdersFilterBottomSheet(
                visible = isFilterSheetVisible,
                initialFilter = state.filter,
                onDismiss = {
                    isFilterSheetVisible = false
                },
                onApply = {
                    isFilterSheetVisible = false
                    onAction(ManufacturerOrdersUiAction.UpdateFilter(it))
                }
            )
            SaveManufacturerOrderBottomSheet(
                visible = isSaveOrderSheetVisible,
                initialOrder = selectedOrder,
                onDismiss = {
                    isSaveOrderSheetVisible = false
                },
                onSave = {
                    isSaveOrderSheetVisible = false
                    onAction(ManufacturerOrdersUiAction.SaveManufacturerOrder(it))
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteVisible,
                onDismiss = {
                    isConfirmDeleteVisible = false
                },
                onConfirm = {
                    isConfirmDeleteVisible = false
                    onAction(ManufacturerOrdersUiAction.DeleteManufacturerOrder(selectedOrder))
                },
                label = stringResource(R.string.manufacturer_order)
            )
        }
    }
}