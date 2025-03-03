package com.zaed.distributor.ui.addproductsale

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import com.zaed.distributor.ui.addproductsale.components.PreviewSaleItem
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
                            }
//                            onRemoveProduct = {
//                                onAction(AddProductSaleUiAction.OnRemoveProduct(it))
//                            },
//                            onEditProduct = {
//                                onAction(AddProductSaleUiAction.OnEditProduct(it))
//                            }
                        )
                    }

                    1 -> {
                        PreviewSaleContent(
                            sale = state.sale,
                            onUpdateProducts = {
                                onAction(AddProductSaleUiAction.OnUpdateProducts(it))
                            }
                        )
                    }

                    2 -> {
                        SelectPaymentsContent(
                            totalAmount = state.totalAmount,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewSaleContent(
    modifier: Modifier = Modifier,
    sale: WholesaleProductSale = WholesaleProductSale(),
    onUpdateProducts: (products: List<Product>) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        var initialSale by remember { mutableStateOf(sale) }
        var editProductSheet by remember { mutableStateOf(false) }
        var selectedProduct by remember { mutableStateOf<Product?>(null) }
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(sale.products) { product ->
                PreviewSaleItem(
                    product = product,
                    onShowProductDetails = {
                        editProductSheet = true
                        selectedProduct = it
                    }
                )
                HorizontalDivider()
            }
        }
        AnimatedVisibility(editProductSheet) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                onDismissRequest = {
                    editProductSheet = false
                    selectedProduct = null
                },
                dragHandle = {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = selectedProduct?.name ?: ""
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    editProductSheet = false
                                    selectedProduct = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الكمية",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(
                            onClick = {
                                if(selectedProduct?.quantity==1) return@Surface
                                initialSale = initialSale.copy(
                                    products = initialSale.products.map {
                                        if (it.categoryId == selectedProduct?.categoryId && it.gramPrice == selectedProduct?.gramPrice) {
                                            it.copy(quantity = it.quantity - 1)
                                        } else {
                                            it
                                        }

                                    }
                                )
                            },
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            modifier = modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Sale icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Surface(
                            modifier
                                .padding(end = 4.dp)
                                .width(48.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp)
                                )
                        ) {
                            Text(
                                text = sale.products.find { it.categoryId == selectedProduct?.categoryId && it.gramPrice == selectedProduct?.gramPrice }?.quantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Surface(
                            onClick = {
                                initialSale = initialSale.copy(
                                    products = initialSale.products.map {
                                        if (it.categoryId == selectedProduct?.categoryId && it.gramPrice == selectedProduct?.gramPrice) {
                                            it.copy(quantity = it.quantity + 1)
                                        } else {
                                            it
                                        }

                                    }
                                )
                            },
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            modifier = modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Sale icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الثمن",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        NumberInputTextField(
                            modifier = Modifier.width(120.dp),
                            shape = RoundedCornerShape(4.dp),
                            withBorder = true,
                            value = selectedProduct?.gramPrice?:0.0,
                            onValueChange = {newPrice->
                                initialSale = initialSale.copy(
                                    products = initialSale.products.map{
                                        if (it.categoryId == selectedProduct?.categoryId && it.gramPrice == selectedProduct?.gramPrice) {
                                            it.copy(gramPrice = newPrice)
                                        } else {
                                            it
                                        }
                                    }
                                )
                            }

                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        ) {
                            Text(
                                text = "تأكيد"
                            )
                        }
                        FilledTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        ) {
                            Text(
                                text = "حذف"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PreviewSaleContentPreview() {
    PreviewSaleContent(
        sale = WholesaleProductSale(
            products = listOf(
                Product(name = "Gold", quantity = 1, gramPrice = 100.0),
                Product(name = "Silver", quantity = 2, gramPrice = 50.0),
            )
        ),
    )


}