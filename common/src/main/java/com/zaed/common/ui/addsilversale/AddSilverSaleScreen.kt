package com.zaed.common.ui.addsilversale

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
import com.zaed.common.ui.addGoldSale.components.SelectSilverContent
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.components.PreviewSaleContent
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.common.ui.components.SaleSummaryContent
import com.zaed.common.ui.components.SelectPaymentsContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddSilverSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddSilverSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToSilverSaleDetails: (saleId: String) -> Unit,
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
            onNavigateToSilverSaleDetails(state.sale.id)
        }
    }  
    AddSilverSaleScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                AddSilverSaleUiAction.OnAddNewCustomerClicked -> onNavigateToAddCustomer()
                AddSilverSaleUiAction.OnBackClicked -> onBackClicked()
                AddSilverSaleUiAction.OpenDrawer -> onOpenDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    ) 
}

@Composable
private fun AddSilverSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddSilverSaleUiState,
    onAction: (AddSilverSaleUiAction) -> Unit,
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
                onOpenDrawer = {onAction(AddSilverSaleUiAction.OpenDrawer)}
            ) {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                } else {
                    onAction(AddSilverSaleUiAction.OnBackClicked)
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
                        SelectSilverContent(
                            sale = state.sale,
                            availableCategories = state.categories,
                            onAddSilver = {
                                onAction(AddSilverSaleUiAction.OnAddProduct(it))
                            },
                            onNext = {
                                if (pagerState.currentPage == 3) {
                                    onAction(AddSilverSaleUiAction.OnSubmitClicked)
                                } else {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            onRemoveSilver = {
                                onAction(AddSilverSaleUiAction.OnRemoveProduct(it))
                            }
                        )
                    }

                    1 -> {
                        PreviewSaleContent(
                            transaction = state.sale,
                            onUpdateProduct = {
                                onAction(AddSilverSaleUiAction.OnAddProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddSilverSaleUiAction.OnDeleteProduct(it))
                            },
                            deleteAllProducts = {
                                onAction(AddSilverSaleUiAction.OnDeleteAllProducts)
                                if (pagerState.currentPage > 0) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                } else {
                                    onAction(AddSilverSaleUiAction.OnBackClicked)
                                }
                            },
                            query = state.customerSearchQuery,
                            onQueryChanged = {
                                onAction(AddSilverSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            selectedAccount = state.selectedCustomer,
                            suggestedAccounts = state.suggestedCustomers,
                            onAddNewAccount = {
                                onAction(AddSilverSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddSilverSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
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
                                onAction(AddSilverSaleUiAction.OnCustomerSearchQueryChanged(it))
                            },
                            onUpdateDiscount = {
                                onAction(AddSilverSaleUiAction.OnUpdateDiscount(it))
                            },
                            suggestedAccount = state.suggestedCustomers,
                            onAddNewCustomer = {
                                onAction(AddSilverSaleUiAction.OnAddNewCustomerClicked)
                            },
                            onAccountSelected = {
                                onAction(AddSilverSaleUiAction.OnCustomerSelected(it as WholeSaleCustomer))
                            },
                            onAddPayment = {
                                onAction(AddSilverSaleUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddSilverSaleUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddSilverSaleUiAction.OnRemovePayment(it.id))
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
                                onAction(AddSilverSaleUiAction.OnSubmitClicked)
                            },
                            productType = ProductType.SILVER
                        )
                    }
                }
            }
        }
    }

}