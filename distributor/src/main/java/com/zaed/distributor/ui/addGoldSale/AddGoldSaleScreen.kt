package com.zaed.distributor.ui.addGoldSale

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.distributor.ui.addGoldSale.components.SelectGoldContent
import com.zaed.distributor.ui.addGoldSale.components.SelectGoldPaymentsContent
import com.zaed.common.ui.components.SaleSummaryContent
import com.zaed.common.ui.components.SelectCustomerContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddGoldSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddGoldSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToGoldSaleDetails: (saleId: String) -> Unit,
    onNavigateToAddCustomer: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (true) {
        viewModel.init(saleId)
    }
    LaunchedEffect (state.isFinished){
        if(state.isFinished){
            onNavigateToGoldSaleDetails(state.sale.id)
        }
    }
    AddGoldSaleScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                AddGoldSaleUiAction.OnAddNewCustomerClicked -> onNavigateToAddCustomer()
                AddGoldSaleUiAction.OnBackClicked -> onBackClicked()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@Composable
private fun AddGoldSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddGoldSaleUiState,
    onAction: (AddGoldSaleUiAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 4 }
    val progress by remember {
        derivedStateOf {
            (pagerState.currentPage + 1).toFloat() / (pagerState.pageCount + 1)
        }
    }.let { progressState ->
        animateFloatAsState(
            targetValue = progressState.value,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = "linear progress indicator"
        )
    }

    BackHandler { 
        if(pagerState.currentPage > 0){
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onAction(AddGoldSaleUiAction.OnBackClicked)
        }
    }
    Scaffold (
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress
            ) {
                onAction(AddGoldSaleUiAction.OnBackClicked)
            }
        },
        bottomBar = {
            BottomAppBar (
                contentPadding = PaddingValues(0.dp),
                containerColor = MaterialTheme.colorScheme.surface,
            ){
            Surface (
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                shadowElevation = 8.dp
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        modifier = Modifier
                            .weight(1f).heightIn(min = 48.dp),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        enabled = pagerState.currentPage > 0
                    ) {
                        Text(
                            text = stringResource(R.string.previous),
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                        enabled = !state.isLoading,
                        onClick = {
                            if (pagerState.currentPage == 3) {
                                onAction(AddGoldSaleUiAction.OnSubmitClicked)
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (pagerState.currentPage == 3) stringResource(R.string.submit) else stringResource(
                                    R.string.continue_
                                )
                            )
                            AnimatedVisibility(state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when(page){
                    0 -> {
                        //select customer
                        SelectCustomerContent(
                            query = state.customerSearchQuery,
                            onQueryChanged = {
                                onAction(AddGoldSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            selectedCustomer = state.selectedCustomer,
                            suggestedCustomers = state.suggestedCustomers,
                            onAddNewCustomer = {
                                onAction(AddGoldSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onCustomerSelected = {
                                onAction(AddGoldSaleUiAction.OnCustomerSelected(it))
                            }
                        )
                    }
                    1 -> {
                        //add products
                        SelectGoldContent(
                            sale = state.sale.products,
                            onAddGold = {
                                onAction(AddGoldSaleUiAction.OnAddProduct(it))
                            },
                            onRemoveGold = {
                                onAction(AddGoldSaleUiAction.OnRemoveProduct(it))
                            },
                            onEditGold = {
                                onAction(AddGoldSaleUiAction.OnEditProduct(it))
                            }
                        )
                    }
                    2 -> {
                        SelectGoldPaymentsContent(
                            goldCost= state.totalAmount,
                            laborCost = state.laborCost,
                            moneyPayments = state.totalMoneyPaid,
                            goldPayments = state.totalGoldAmount,
                            totalGoldPaid = state.totalGoldPrice,
                            totalPaid = state.totalPaid,
                            payments = state.cashPayments + state.goldPayments,
                            onAddPayment = {
                                onAction(AddGoldSaleUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddGoldSaleUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddGoldSaleUiAction.OnRemovePayment(it.id))
                            }
                        )
                    }
                    3 -> {
                        SaleSummaryContent(
                            selectedCustomer = state.selectedCustomer,
                            customer = state.selectedCustomer,
                            products = state.sale.products,
                            totalPaid = state.totalPaid ,
                            totalAmount = state.sale.totalAmount,
                            payments = state.cashPayments
                        )
                    }
                }
            }
        }
    }

}