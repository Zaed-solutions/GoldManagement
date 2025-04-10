package com.zaed.common.ui.addGoldSale

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.getGoldSalePayments
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.data.model.sale.Karat
import com.zaed.common.ui.addGoldSale.components.SelectGoldContent
import com.zaed.common.ui.components.PaymentTypes
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
    Log.d("CREATED89",state.payments.map { it.amount to it.type }.toString())
    Log.d("CREATED89",state.totalMoneyPaid.toString())
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
    val pagerState = rememberPagerState { 5 }
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
        if (pagerState.currentPage ==3  && state.sale.id.isNotBlank()) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 2)
            }

        } else if (pagerState.currentPage ==3){
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
            onAction(AddGoldSaleUiAction.ResetPayments)
        }else if (pagerState.currentPage > 0){
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }else {
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
                            availableGrams = state.categories.sumOf { it.availableGrams },
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
                            isGoldSale = true,
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
                                    if (state.sale.id.isBlank()) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }else{
                                        pagerState.animateScrollToPage(pagerState.currentPage + 2)
                                    }
                                }
                            }
                        )
                    }
                    2->{
                        SelectPaymentType(
                            onTypeSelected = {
                                Log.d("258369",it.toString())
                                onAction(AddGoldSaleUiAction.OnUpdatePaymentType(it))
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }
                    3 -> {
                        SelectPaymentsContent(
                            totalMoneyAmount = state.totalMoneyAmount,
                            totalGoldAmount = state.totalGoldAmount,
                            totalMoneyPaid = state.totalMoneyPaid + state.totalMoneyFuturePaid,
                            totalGoldPaid = state.totalGoldPaid + state.totalGoldFuturePaid,
                            payments = state.payments,
                            discount = state.sale.discount,
                            onUpdateDiscount = {
                                onAction(AddGoldSaleUiAction.OnUpdateDiscount(it))
                            },
                            selectedAccount = state.selectedCustomer,
                            currentUser = state.currentUser,
                            products = state.sale.products,
                            payWithGold = !state.sale.payWithCash,
                            query = state.customerSearchQuery,
                            paymentsTypes = if (state.sale.payWithCash) getProductSalePayments() else  getGoldSalePayments(),
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
                                Log.d("payment550","in the screen"+it.toString())
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

                    4 -> {
                        SaleSummaryContent(
                            account = state.selectedCustomer,
                            products = state.sale.products,
                            payWithGold = !state.sale.payWithCash,
                            totalMoneyPaid = state.totalMoneyPaid,
                            totalGoldPaid = state.totalGoldPaid,
                            isKaratUnSpecified = state.payments.filterIsInstance<GoldPayment>().any { it.givenGoldKarat==Karat.NOT_SPECIFIED },
                            totalAmount = state.sale.totalAmount,
                            isLoading = state.isLoading,
                            onCreate = {
                                onAction(AddGoldSaleUiAction.OnSubmitClicked)
                            },

                            productType = state.sale.productType
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentType(onTypeSelected: (Boolean) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        text = stringResource(R.string.select_payment_type),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentTypes(
                onPaymentTypeSelected = onTypeSelected
            )
        }
    }
}
