package com.zaed.common.ui.saledetails.productsaledetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.components.ChangeLogList
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.CustomerInfoSection
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.saledetails.productsaledetails.components.PaymentsTable
import com.zaed.common.ui.saledetails.productsaledetails.components.ProductSaleDetailsTopBar
import com.zaed.common.ui.saledetails.productsaledetails.components.SaleInfoSection
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun ProductSaleDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductSaleDetailsViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onNavigateToEditSale: (String) -> Unit,
    saleId: String = "",
    onNavigateToCustomerDetails: (String) -> Unit,
    isAdmin: Boolean = false
) {
    LaunchedEffect(true) {
        viewModel.init(saleId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaleDeleted) {
        if (state.isSaleDeleted) {
            onBackClicked()
        }
    }
    ProductSaleDetailsContent(
        state = state,
        isAdmin = isAdmin
    ) { action ->
        when (action) {
            SaleDetailsUiAction.OnBackClicked -> onBackClicked()
            SaleDetailsUiAction.OnCustomerClicked -> onNavigateToCustomerDetails(state.sale.accountId)
            SaleDetailsUiAction.OnEditClicked -> onNavigateToEditSale(saleId)
            else -> viewModel.handleAction(action)
        }
    }
}

@Composable
private fun ProductSaleDetailsContent(
    modifier: Modifier = Modifier,
    state: ProductSaleDetailsUiState,
    isAdmin: Boolean = false,
    onAction: (SaleDetailsUiAction) -> Unit,
) {
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductSaleDetailsTopBar(
                receiptNumber = state.sale.receiptNumber,
                onBackClicked = {
                    onAction(SaleDetailsUiAction.OnBackClicked)
                },
                onEditClicked = {
                    onAction(SaleDetailsUiAction.OnEditClicked)
                },
                onDeleteClicked = {
                    isConfirmDeleteVisible = true
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // sale info
            SaleInfoSection(
                receiptNumber = state.sale.receiptNumber,
                createdAt = state.sale.createdAt,
                totalPrice = state.sale.totalAmount,
                paymentStatus = state.sale.paymentStatus,
                receiptStatus = state.sale.receiptStatus,
                onRequestReceipt = {
                    onAction(SaleDetailsUiAction.OnRequestReceipt)
                }
            )
            if (state.customer.id.isNotBlank()) {
                // customer info
                CustomerInfoSection(
                    customerName = state.sale.customerName,
                    customerDebt = state.customer.moneyDebtAmount,
                    onCustomerClicked = {
                        onAction(SaleDetailsUiAction.OnCustomerClicked)
                    }
                )
            }
            //products
            TitledSection(
                title = stringResource(R.string.products)
            ) {
                ProductsTable(
                    products = state.sale.products,
                    isModifyEnabled = false,
                    productType = state.sale.productType
                )
            }
            //payments
            TitledSection(
                title = stringResource(R.string.payments)
            ) {
                PaymentsTable(
                    payments = state.cashPayments
                )
            }

            //LOGS
            if (isAdmin && state.sale.logs.isNotEmpty()){
                TitledSection(
                    title = stringResource(R.string.change_logs)
                ) {
                    ChangeLogList(
                        changeLogs = state.sale.logs
                    )
                }
            }
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteVisible,
                onDismiss = {
                    isConfirmDeleteVisible = false
                },
                onConfirm = {
                    onAction(SaleDetailsUiAction.OnDeleteSale)
                    isConfirmDeleteVisible = false
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    ProductSaleDetailsContent(
        onAction = {},
        state = ProductSaleDetailsUiState(
            sale = WholesaleTransaction(
                customerName = "Ali",
                createdAt = Date(),
                receiptNumber = "123456",
                products = listOf(
                    Product(
                        name = "Gold",
                        quantity = 5,
                        gramPrice = 100.0,
                        grams = 10.0
                    ),
                    Product(
                        name = "Silver",
                        quantity = 2,
                        gramPrice = 50.0,
                        grams = 5.0
                    )
                )
            )
        )
    )
}