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
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.components.PreviewSaleContent
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.common.ui.components.SaleSummaryContent
import com.zaed.common.ui.components.SelectPaymentsContent
import com.zaed.common.ui.components.SelectProductsContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProductSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddProductSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToProductSaleDetails: (saleId: String) -> Unit,
    onNavigateToAddCustomer: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.init(saleId)
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            Log.d("CREATED",state.sale.id)
            onNavigateToProductSaleDetails(state.sale.id)
        }
    }
    AddProductSaleScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                AddProductSaleUiAction.OnAddNewCustomerClicked -> onNavigateToAddCustomer()
                AddProductSaleUiAction.OnBackClicked -> onBackClicked()
                AddProductSaleUiAction.OpenDrawer -> onOpenDrawer()
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
        }
    }
    Scaffold(
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress,
                firstScreen = pagerState.currentPage == 0,
                onOpenDrawer = {onAction(AddProductSaleUiAction.OpenDrawer)}
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
                    0 -> {
                        //add products
                        SelectProductsContent(
                            categories = state.categories,
                            transaction = state.sale,
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
                            transaction = state.sale,
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
                            selectedAccount = state.selectedCustomer,
                            suggestedAccounts = state.suggestedCustomers,
                            onAddNewAccount = {
                                onAction(AddProductSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddProductSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
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
                            totalMoneyAmount = state.sale.totalAmount,
                            payments = state.payments,
                            totalMoneyPaid = state.totalPaid + state.futurePaid,
                            discount = state.sale.discount,
                            selectedAccount = state.selectedCustomer,
                            currentUser = state.currentUser,
                            query = state.customerSearchQuery,
                            onQueryChanged = {
                                onAction(AddProductSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            onUpdateDiscount = {
                                onAction(AddProductSaleUiAction.OnUpdateDiscount(it))
                            },
                            suggestedAccount = state.suggestedCustomers,
                            onAddNewCustomer = {
                                onAction(AddProductSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddProductSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
                            },
                            onAddPayment = {
                                onAction(AddProductSaleUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddProductSaleUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddProductSaleUiAction.OnRemovePayment(it.id))
                            },
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }

                    3 -> {
                        SaleSummaryContent(
                            account = state.selectedCustomer,
                            products = state.sale.products,
                            totalMoneyPaid = state.totalPaid,
                            totalAmount = state.sale.totalAmount,
                            isLoading = state.isLoading,
                            onCreate = {
                                onAction(AddProductSaleUiAction.OnSubmitClicked)
                            },
                            productType = ProductType.PRODUCT
                        )
                    }
                }
            }
        }
    }

}

