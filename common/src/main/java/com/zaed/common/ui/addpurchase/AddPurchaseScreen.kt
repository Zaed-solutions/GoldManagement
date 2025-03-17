package com.zaed.common.ui.addpurchase

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addGoldSale.components.SelectGoldContent
import com.zaed.common.ui.addpurchase.components.SelectProductType
import com.zaed.common.ui.components.PreviewSaleContent
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.common.ui.components.SaleSummaryContent
import com.zaed.common.ui.components.SelectPaymentsContent
import com.zaed.common.ui.components.SelectProductsContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddPurchaseScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPurchaseViewModel = koinViewModel(),
    purchaseId: String = "",
    onBackClicked: () -> Unit,
    navigateToPurchaseDetails: (purchaseId: String) -> Unit,
    onNavigateToAddSupplier: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.init(purchaseId)
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            Log.d("CREATED", state.purchase.id)
            navigateToPurchaseDetails(state.purchase.id)
        }
    }
    AddPurchaseScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                AddPurchaseUiAction.OnBackClicked -> onBackClicked()
                AddPurchaseUiAction.OpenDrawer -> onOpenDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

enum class ProductType(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
) {
    GOLD(
        R.string.gold,
        R.drawable.ic_gold
    ),
    INGOT(
        R.string.ingots,
        R.drawable.ic_ingot
    ),
    PRODUCT(
        R.string.products,
        R.drawable.ic_cart
    )
}

@Composable
private fun AddPurchaseScreenContent(
    modifier: Modifier = Modifier,
    state: AddPurchaseUiState,
    onAction: (AddPurchaseUiAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    Log.d(
        "find the issue",
        "fetchCurrentUser: screen"
    )
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
    var selectedProductType by remember {
        mutableStateOf(ProductType.PRODUCT)
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
                onOpenDrawer = { onAction(AddPurchaseUiAction.OpenDrawer) }
            ) {
                if (pagerState.currentPage == 1) {
                    onAction(AddPurchaseUiAction.ReselectProductType)
                } else if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                } else {
                    onAction(AddPurchaseUiAction.OnBackClicked)
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
                        SelectProductType { type ->
                            onAction(AddPurchaseUiAction.OnProductTypeSelected(type))
                            selectedProductType = when (type) {
                                ProductType.GOLD -> ProductType.GOLD
                                ProductType.INGOT -> ProductType.INGOT
                                ProductType.PRODUCT -> ProductType.PRODUCT
                            }
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }

                    1 -> {
                        when (selectedProductType) {
                            ProductType.GOLD -> {
                                SelectGoldContent(
                                    sale = state.purchase as WholesaleTransaction,
                                    onAddGold = {
                                        onAction(AddPurchaseUiAction.OnAddProduct(it))
                                    },
                                    onRemoveGold = {productId->
                                        state.purchase.products.firstOrNull { productId == it.id }?.let {
                                            onAction(AddPurchaseUiAction.OnDeleteProduct(it))
                                        }
                                    },
                                    onNext = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    },
                                )
                            }

                            ProductType.INGOT -> {

                            }

                            ProductType.PRODUCT -> {
                                SelectProductsContent(
                                    categories = state.categories,
                                    transaction = state.purchase,
                                    onAddProduct = {
                                        onAction(AddPurchaseUiAction.OnAddProduct(it))
                                    },
                                    onNext = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    },
                                    onDeleteProduct = {
                                        onAction(AddPurchaseUiAction.OnDeleteProduct(it))
                                    },
                                    isPurchase = true,
                                    onAddNewCategory = {
                                        onAction(AddPurchaseUiAction.OnAddNewCategory(it))
                                    }
                                )
                            }
                        }

                    }

                    2 -> {
                        PreviewSaleContent(
                            transaction = state.purchase,
                            onUpdateProduct = {
                                onAction(AddPurchaseUiAction.OnAddProduct(it))
                            },
                            onDeleteProduct = {
                                onAction(AddPurchaseUiAction.OnDeleteProduct(it))
                            },
                            deleteAllProducts = {
                                onAction(AddPurchaseUiAction.OnDeleteAllProducts)
                                if (pagerState.currentPage > 0) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                } else {
                                    onAction(AddPurchaseUiAction.OnBackClicked)
                                }
                            },
                            query = state.supplierSearchQuery,
                            onQueryChanged = {
                                onAction(AddPurchaseUiAction.OnSupplierSearchQueryChanged(it))
                            },
                            selectedAccount = state.selectedSupplier,
                            suggestedAccounts = state.suggestedSuppliers,
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            isAdmin = state.isAdmin,
                            isLoading = state.isLoading,
                            supplierSearchQuery = state.supplierSearchQuery,
                            onUpdateSupplierSearchQuery = {
                                onAction(AddPurchaseUiAction.OnSupplierSearchQueryChanged(it))
                            },
                            filteredSuppliers = state.suggestedSuppliers,
                            onSupplierClicked = {
                                onAction(AddPurchaseUiAction.OnSupplierSelected(it))
                            },
                            onAddSupplier = {
                                onAction(AddPurchaseUiAction.OnAddNewSupplierClicked(it))
                            },
                        )
                    }

                    3 -> {
                        SelectPaymentsContent(
                            totalAmount = state.purchase.totalAmount,
                            payments = state.payments,
                            selectedAccount = state.selectedSupplier,
                            paymentsTypes = listOf(
                                PaymentType.CHEQUE,
                                PaymentType.BANK_TRANSFER,
                                PaymentType.CASH,
                                PaymentType.MANAGER_CHEQUES,
                            ),
                            query = state.supplierSearchQuery,
                            onQueryChanged = {
                                onAction(AddPurchaseUiAction.OnSupplierSearchQueryChanged(it))
                            },
                            suggestedAccount = state.suggestedSuppliers,

                            onAccountSelected = {
                                onAction(AddPurchaseUiAction.OnSupplierSelected(it.id))
                            },
                            onAddPayment = {
                                onAction(AddPurchaseUiAction.OnAddPayment(it))
                            },
                            onEditPayment = {
                                onAction(AddPurchaseUiAction.OnEditPayment(it))
                            },
                            onRemovePayment = {
                                onAction(AddPurchaseUiAction.OnRemovePayment(it.id))
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
                            account = state.selectedSupplier,
                            products = state.purchase.products,
                            totalPaid = state.totalPaid,
                            totalAmount = state.purchase.totalAmount,
                            isLoading = state.isLoading,
                            isPurchase = true,
                            onCreate = {
                                onAction(AddPurchaseUiAction.OnSubmitClicked)
                            }
                        )
                    }
                }
            }
        }
    }

}

