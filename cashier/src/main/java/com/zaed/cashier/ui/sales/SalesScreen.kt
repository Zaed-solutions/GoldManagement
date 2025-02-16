package com.zaed.cashier.ui.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.R
import com.zaed.cashier.ui.sales.components.SalesList
import com.zaed.common.data.model.StoreSale
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.common.ui.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel = koinViewModel(),
    onNavigateToSaleDetails: (String) -> Unit,
    onNavigateToAddSale: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SalesScreenContent(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when(action){
                is SalesUiAction.OnSaleClicked -> onNavigateToSaleDetails(action.saleId)
                is SalesUiAction.AddSaleClicked -> onNavigateToAddSale()
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
        topBar = {
            TopAppBar(
                title= {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
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
            //search bar
            SearchBar(
                modifier = Modifier.padding(16.dp),
                query = state.searchQuery,
                onQueryChanged = { onAction(SalesUiAction.UpdateSearchQuery(it)) }
            )
            //sales list
            SalesList(
                isLoading = state.isLoading,
                sales = state.sales,
                onSaleClicked = {
                    onAction(SalesUiAction.OnSaleClicked(it))
                },
                onDeleteSale = {
                    selectedSale = it
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


