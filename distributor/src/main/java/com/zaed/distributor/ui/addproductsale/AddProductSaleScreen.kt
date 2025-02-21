package com.zaed.distributor.ui.addproductsale

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.distributor.ui.addproductsale.components.SaleSummaryContent
import com.zaed.distributor.ui.addproductsale.components.SelectCustomerContent
import com.zaed.distributor.ui.addproductsale.components.SelectPaymentsContent
import com.zaed.distributor.ui.addproductsale.components.SelectProductsContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProductSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddProductSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToProductSaleDetails: (saleId: String) -> Unit,
    onNavigateToAddCustomer: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (true) {
        viewModel.init(saleId)
    }
    LaunchedEffect (state.isFinished){
        if(state.isFinished){
            onNavigateToProductSaleDetails(state.sale.id)
        }
    }
    AddProductSaleScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                AddProductSaleUiAction.OnAddNewCustomerClicked -> onNavigateToAddCustomer()
                AddProductSaleUiAction.OnBackClicked -> onBackClicked()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@Composable
private fun AddProductSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddProductSaleUiState,
    onAction: (AddProductSaleUiAction) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pagerState = rememberPagerState { 4 }
    val progress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1).toFloat() /(pagerState.pageCount + 1),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "linear progress inicator"
    )
    BackHandler { 
        if(pagerState.currentPage > 0){
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onAction(AddProductSaleUiAction.OnBackClicked)
        }
    }
    Scaffold (
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress
            ) {
                onAction(AddProductSaleUiAction.OnBackClicked)
            }
        },
        bottomBar = {
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
                ){
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
                        onClick = {
                            if(pagerState.currentPage == 3){
                                onAction(AddProductSaleUiAction.OnSubmitClicked)
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage+1)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = if(pagerState.currentPage == 3) stringResource(R.string.submit) else stringResource(R.string.continue_)
                        )
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
                                onAction(AddProductSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            selectedCustomer = state.selectedCustomer,
                            suggestedCustomers = state.suggestedCustomers,
                            onAddNewCustomer = {
                                onAction(AddProductSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onCustomerSelected = {
                                onAction(AddProductSaleUiAction.OnCustomerSelected(it))
                            }
                        )
                    }
                    1 -> {
                        //add products
                        SelectProductsContent(
                            categories = state.categories,
                            sale = state.sale,
                            onAddProduct = {
                                onAction(AddProductSaleUiAction.OnAddProduct(it))
                            },
                            onRemoveProduct = {
                                onAction(AddProductSaleUiAction.OnRemoveProduct(it))
                            },
                            onEditProduct = {
                                onAction(AddProductSaleUiAction.OnEditProduct(it))
                            }
                        )
                    }
                    2 -> {
                        SelectPaymentsContent(
                            totalAmount = state.totalAmount,
                            totalPaid = state.totalPaid,
                            payments = state.payments,
                            onAddPayment = {
                                onAction(AddProductSaleUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddProductSaleUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddProductSaleUiAction.OnRemovePayment(it.id))
                            }
                        )
                    }
                    3 -> {
                        SaleSummaryContent(
                            customer = state.selectedCustomer,
                            sale = state.sale,
                            totalPaid = state.totalPaid,
                            totalAmount = state.totalAmount,
                            payments = state.payments
                        )
                    }
                }
            }
        }
    }

}