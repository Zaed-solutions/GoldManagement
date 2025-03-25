package com.zaed.common.ui.saledetails.goldsaledetails

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
import com.zaed.common.ui.components.ChangeLogList
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.CustomerInfoSection
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import com.zaed.common.ui.saledetails.productsaledetails.components.PaymentsTable
import com.zaed.common.ui.saledetails.productsaledetails.components.ProductSaleDetailsTopBar
import com.zaed.common.ui.saledetails.productsaledetails.components.SaleInfoSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun GoldSaleDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: GoldSaleDetailsViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onNavigateToEditSale: (String) -> Unit,
    navigateToCustomerDetails: (String) -> Unit,
    saleId: String = "",
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
    GoldSaleDetailsContent(
        state = state,
        isAdmin = isAdmin
    ) { action ->
        when (action) {
            SaleDetailsUiAction.OnBackClicked -> onBackClicked()
            SaleDetailsUiAction.OnEditClicked -> onNavigateToEditSale(saleId)
            SaleDetailsUiAction.OnCustomerClicked -> navigateToCustomerDetails(state.customer.id)
            else -> viewModel.handleAction(action)
        }
    }
}

@Composable
private fun GoldSaleDetailsContent(
    modifier: Modifier = Modifier,
    state: GoldSaleDetailsUiState,
    isAdmin: Boolean,
    onAction: (SaleDetailsUiAction) -> Unit,
) {
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductSaleDetailsTopBar(
                onBackClicked = {
                    onAction(SaleDetailsUiAction.OnBackClicked)
                },
                onEditClicked = {
                    onAction(SaleDetailsUiAction.OnEditClicked)
                },
                onDeleteClicked = {
                    isConfirmDeleteVisible = true
                },
                receiptNumber = state.sale.receiptNumber
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
            // customer info
            CustomerInfoSection(
                customerName = state.sale.customerName,
                customerDebt = state.customer.moneyDebtAmount,
                onCustomerClicked = {
                    onAction(SaleDetailsUiAction.OnCustomerClicked)
                }
            )
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
                    payments = state.payments
                )
            }
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    GoldSaleDetailsContent(
        onAction = {},
        isAdmin = true,
        state = GoldSaleDetailsUiState()
    )
}