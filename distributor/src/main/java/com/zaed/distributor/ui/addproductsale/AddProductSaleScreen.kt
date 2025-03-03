package com.zaed.distributor.ui.addproductsale

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.distributor.ui.addproductsale.components.PreviewSaleContent
import com.zaed.distributor.ui.addproductsale.components.SaleSummaryContent
import com.zaed.distributor.ui.addproductsale.components.SelectPaymentsContent
import com.zaed.distributor.ui.addproductsale.components.SelectProductsContent
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
    LaunchedEffect(true) {
        viewModel.init(saleId)
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onNavigateToProductSaleDetails(state.sale.id)
        }
    }
    AddProductSaleScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
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
) {
    val scope = rememberCoroutineScope()
    Log.d(
        "find the issue",
        "fetchCurrentUser: screen"
    )
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
        if (pagerState.currentPage > 0) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onAction(AddProductSaleUiAction.OnBackClicked)
        }
    }
    Scaffold(
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress
            ) {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                } else {
                    onAction(AddProductSaleUiAction.OnBackClicked)
                }
            }
        },
//        bottomBar = {
//            BottomAppBar(
//                contentPadding = PaddingValues(0.dp),
//                containerColor = MaterialTheme.colorScheme.surface,
//            ) {
//                Surface(
//                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//                    shadowElevation = 8.dp
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        FilledTonalButton(
//                            modifier = Modifier
//                                .weight(1f)
//                                .heightIn(min = 48.dp),
//                            onClick = {
//                                scope.launch {
//                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
//                                }
//                            },
//                            enabled = pagerState.currentPage > 0
//                        ) {
//                            Text(
//                                text = stringResource(R.string.previous),
//                            )
//                        }
//                        Button(
//                            modifier = Modifier
//                                .weight(1f)
//                                .heightIn(min = 48.dp),
//                            enabled = !state.isLoading,
//                            onClick = {
//                                if (pagerState.currentPage == 3) {
//                                    onAction(AddProductSaleUiAction.OnSubmitClicked)
//                                } else {
//                                    scope.launch {
//                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                                    }
//                                }
//                            }
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                Text(
//                                    text = if (pagerState.currentPage == 3) stringResource(R.string.submit) else stringResource(
//                                        R.string.continue_
//                                    )
//                                )
//                                AnimatedVisibility(state.isLoading) {
//                                    CircularProgressIndicator(
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when (page) {
//                    0 -> {
//                        //select customer
//                        SelectCustomerContent(
//                            query = state.customerSearchQuery,
//                            onQueryChanged = {
//                                onAction(AddProductSaleUiAction.OnCustomerSearchQueryChanged(it))
//                            },
//                            selectedCustomer = state.selectedCustomer,
//                            suggestedCustomers = state.suggestedCustomers,
//                            onAddNewCustomer = {
//                                onAction(AddProductSaleUiAction.OnAddNewCustomerClicked)
//                            },
//                            onCustomerSelected = {
//                                onAction(AddProductSaleUiAction.OnCustomerSelected(it))
//                            }
//                        )
//                    }

                    0 -> {
                        //add products
                        SelectProductsContent(
                            categories = state.categories,
                            sale = state.sale,
                            onAddProduct = {
                                onAction(AddProductSaleUiAction.OnAddProduct(it))
                            },
                            onNext = {
                                if (pagerState.currentPage == 3) {
                                    onAction(AddProductSaleUiAction.OnSubmitClicked)
                                } else {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            onDeleteProduct = {
                                onAction(AddProductSaleUiAction.OnDeleteProduct(it))
                            }
                        )
                    }

                    1 -> {
                        PreviewSaleContent(
                            sale = state.sale,
                            onUpdateProduct = {
                                onAction(AddProductSaleUiAction.OnAddProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddProductSaleUiAction.OnDeleteProduct(it))
                            },
                            deleteAllProducts = {
                                onAction(AddProductSaleUiAction.OnDeleteAllProducts)
                                if (pagerState.currentPage > 0) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                } else {
                                    onAction(AddProductSaleUiAction.OnBackClicked)
                                }
                            },
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
                            },
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }

                    2 -> {
                        SelectPaymentsContent(
                            totalAmount = state.sale.totalPriceAfterDiscount,
                            totalPaid = state.totalPaid,
                            moneyPayments = state.moneyPayments,
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
                            products = state.sale.products,
                            totalPaid = state.totalPaid,
                            totalAmount = state.totalAmount,
                            moneyPayments = state.moneyPayments
                        )
                    }
                }
            }
        }
    }

}

