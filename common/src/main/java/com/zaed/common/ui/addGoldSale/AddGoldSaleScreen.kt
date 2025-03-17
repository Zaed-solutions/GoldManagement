package com.zaed.common.ui.addGoldSale

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
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.getGoldSalePayments
import com.zaed.common.ui.addGoldSale.components.SelectGoldContent
import com.zaed.common.ui.components.PreviewSaleContent
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.common.ui.components.SaleSummaryContent
import com.zaed.common.ui.components.SelectPaymentsContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddGoldSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddGoldSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToGoldSaleDetails: (saleId: String) -> Unit,
    onNavigateToAddCustomer: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.init(saleId)
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            Log.d("CREATED", state.sale.id)
            onNavigateToGoldSaleDetails(state.sale.id)
        }
    }
    AddGoldSaleScreenContent(
        state = state,
        onOpenDrawer = onOpenDrawer,
        onAction = { action ->
            when (action) {
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
    onOpenDrawer: () -> Unit,
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
        if (pagerState.currentPage > 0) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onAction(AddGoldSaleUiAction.OnBackClicked)
        }
    }
    Scaffold(
        topBar = {
            ProgressIndicatorTopAppBar(
                firstScreen = pagerState.currentPage == 0,
                onOpenDrawer = onOpenDrawer,
                progress = progress
            ) {
                onAction(AddGoldSaleUiAction.OnBackClicked)
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
                        SelectGoldContent(
                            sale = state.sale,
                            onAddGold = {
                                onAction(AddGoldSaleUiAction.OnAddProduct(it))
                            },
                            onRemoveGold = {
                                onAction(AddGoldSaleUiAction.OnRemoveProduct(it))
                            },
                            onNext = {
                                if (pagerState.currentPage == 3) {
                                    onAction(AddGoldSaleUiAction.OnSubmitClicked)
                                } else {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                        )
                    }


                    1 -> {
                        PreviewSaleContent(
                            transaction = state.sale,
                            onUpdateProduct = {
                                onAction(AddGoldSaleUiAction.OnAddProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddGoldSaleUiAction.OnDeleteProduct(it))
                            },
                            deleteAllProducts = {
                                onAction(AddGoldSaleUiAction.OnDeleteAllProducts)
                                if (pagerState.currentPage > 0) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                } else {
                                    onAction(AddGoldSaleUiAction.OnBackClicked)
                                }
                            },
                            query = state.customerSearchQuery,
                            onQueryChanged = {
                                onAction(AddGoldSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            selectedAccount = state.selectedCustomer,
                            suggestedAccounts = state.suggestedCustomers,
                            onAddNewAccount = {
                                onAction(AddGoldSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddGoldSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
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
                            totalAmount = state.sale.totalAmount,
                            payments = state.payments,
                            selectedAccount = state.selectedCustomer,
                            query = state.customerSearchQuery,
                            paymentsTypes = getGoldSalePayments(),
                            onQueryChanged = {
                                onAction(AddGoldSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            suggestedAccount = state.suggestedCustomers,
                            onAddNewCustomer = {
                                onAction(AddGoldSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddGoldSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
                            },
                            onAddPayment = {
                                onAction(AddGoldSaleUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddGoldSaleUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddGoldSaleUiAction.OnRemovePayment(it.id))
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
                            totalPaid = state.totalPaid,
                            isKaratUnSpecified = state.payments.filterIsInstance<GoldPayment>().any { it.pricePerGram==0.0 },
                            totalAmount = state.sale.totalAmount,
                            isLoading = state.isLoading,
                            onCreate = {
                                onAction(AddGoldSaleUiAction.OnSubmitClicked)
                            }
                        )
                    }
                }
            }
        }
    }

}