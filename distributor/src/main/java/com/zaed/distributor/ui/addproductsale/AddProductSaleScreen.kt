package com.zaed.distributor.ui.addproductsale

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
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
    Scaffold (
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress
            ) {
                onAction(AddProductSaleUiAction.OnBackClicked)
            }
        },
        bottomBar = {
            //todo
            Button({
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }) {
                Text("Next")
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
                            totalAmount = state.sale.products.sumOf { it.grams*it.gramPrice },
                            payments = state.payments,
                        )
                    }
                    3 -> {
                        //order summary
                    }
                }
            }
        }
    }

}