package com.zaed.cashier.ui.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.DatedSalesWithSearchSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToSaleDetails: (String) -> Unit,
    onNavigateToAddSale: (saleId: String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SalesScreenContent(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when(action){
                is SalesUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                is SalesUiAction.AddSaleClicked -> onNavigateToAddSale("")
                is SalesUiAction.OnEditSale -> onNavigateToAddSale(action.sale.id)
                SalesUiAction.ShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalesScreenContent(
    modifier: Modifier = Modifier,
    state: SalesUiState,
    onAction: (SalesUiAction) -> Unit
) {
    var isConfirmDeleteSaleSheetVisible by remember{
        mutableStateOf(false)
    }
    var selectedSale by remember {
        mutableStateOf(StoreSale())
    }

    Scaffold (
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title= {
                    Text(
                        text = stringResource(R.string.sales),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SalesUiAction.ShowNavDrawer)
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
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = { onAction(SalesUiAction.AddSaleClicked) },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Sale"
                )
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
       ){
            DatedSalesWithSearchSection(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.isLoading,
                query = state.searchQuery,
                onQueryChanged = { query ->
                    onAction(SalesUiAction.UpdateSearchQuery(query))
                },
                selectedFilter = state.selectedDateFilter,
                onFilterClicked = { filter ->
                    onAction(SalesUiAction.UpdateSelectedDate(filter))
                },
                datedSales = state.datedSales,
                onSaleClicked = { saleId,type ->
                    onAction(SalesUiAction.OnSaleClicked(saleId))
                },
                isEditable = true,
                onEdit = {
                    onAction(SalesUiAction.OnEditSale(it as StoreSale))
                },
                isDeletable = true,
                onDelete = {
                    selectedSale = (it as StoreSale)
                    isConfirmDeleteSaleSheetVisible = true
                }
            )
            AnimatedVisibility(isConfirmDeleteSaleSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        selectedSale = StoreSale()
                        isConfirmDeleteSaleSheetVisible = false
                    },
                    sheetState = rememberModalBottomSheetState()
                ) {
                    ConfirmDeleteDialog(
                        label = stringResource(R.string.sale),
                        onDismiss = {
                            selectedSale = StoreSale()
                            isConfirmDeleteSaleSheetVisible = false
                        },
                        onConfirm = {
                            onAction(SalesUiAction.OnDeleteSale(selectedSale.id))
                            selectedSale = StoreSale()
                            isConfirmDeleteSaleSheetVisible = false
                        },
                    )
                }
            }
        }
    }

}


