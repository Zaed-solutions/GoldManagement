package com.zaed.cashier.ui.addsale

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.ui.addsale.components.SaleSummaryContent
import com.zaed.cashier.ui.addsale.components.SelectCustomerContent
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.ui.components.PreviewSaleContent
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.common.ui.components.SelectProductsContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddSaleViewModel = koinViewModel(),
    saleId: String = "",
    onShowNavDrawer: () -> Unit,
    onNavigateToSaleDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.init(saleId)
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onNavigateToSaleDetails(state.sale.id)
        }
    }
    AddSaleScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                AddSaleUiAction.OnShowNavDrawer -> {
                    onShowNavDrawer()
                }

                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddSaleUiState,
    onAction: (AddSaleUiAction) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
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
        if(pagerState.currentPage > 0) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress,
                firstScreen = pagerState.currentPage == 0,
                onOpenDrawer = {
                    onAction(AddSaleUiAction.OnShowNavDrawer)
                }
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        }
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
                                onAction(AddSaleUiAction.OnAddProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddSaleUiAction.OnDeleteProduct(it))
                            },
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }

                    1 -> {
                        PreviewSaleContent(
                            transaction = state.sale,
                            isSelectCustomerEnabled = false,
                            onUpdateProduct = {
                                onAction(AddSaleUiAction.OnUpdateProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddSaleUiAction.OnDeleteProduct(it))
                            },
                            deleteAllProducts = {
                                onAction(AddSaleUiAction.OnDeleteAllProducts)
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            selectedAccount = state.selectedCustomer,
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }

                    2 -> {
                        SelectCustomerContent(
                            sale = state.sale,
                            onUpdateCustomerName = {
                                onAction(AddSaleUiAction.OnUpdateCustomerName(it))
                            },
                            onUpdateCustomerEmail = {
                                onAction(AddSaleUiAction.OnUpdateCustomerEmail(it))
                            },
                            onUpdateCustomerPhone = {
                                onAction(AddSaleUiAction.OnUpdateCustomerPhone(it))
                            },
                            onContinue = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }

                    3 -> {
                        SaleSummaryContent(
                            sale = state.sale,
                            onSubmit = {
                                onAction(AddSaleUiAction.OnSubmitClicked)
                            },
                            isLoading = state.isLoading
                        )
                    }
                }
            }


        }
    }

}

@Preview(
    showSystemUi = true, showBackground = true,
    device = "spec:parent=pixel_9_pro,navigation=buttons"
)
@Composable
private fun Preview() {
    CashierAppTheme {
        AddSaleScreenContent(
            state = AddSaleUiState(),
            onAction = {}
        )
    }
}