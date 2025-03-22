package com.zaed.common.ui.purchaseDetails

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.ChangeLogList
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.CustomerInfoSection
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.saledetails.productsaledetails.components.PaymentsTable
import com.zaed.common.ui.saledetails.productsaledetails.components.ProductSaleDetailsTopBar
import com.zaed.common.ui.saledetails.productsaledetails.components.SaleInfoSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun PurchaseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: PurchaseDetailsViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onNavigateToEditPurchase: (String) -> Unit,
    purchaseId: String = "",
    onNavigateToSupplierDetails: (String) -> Unit,
    isAdmin: Boolean = false
) {
    LaunchedEffect(true) {
        viewModel.init(purchaseId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isPurchaseDeleted) {
        if (state.isPurchaseDeleted) {
            onBackClicked()
        }
    }
    ProductSaleDetailsContent(
        state = state,
        isAdmin = isAdmin
    ) { action ->
        when (action) {
            SaleDetailsUiAction.OnBackClicked -> onBackClicked()
            SaleDetailsUiAction.OnCustomerClicked -> onNavigateToSupplierDetails(state.purchase.customerId)
            SaleDetailsUiAction.OnEditClicked -> onNavigateToEditPurchase(purchaseId)
            else -> viewModel.handleAction(action)
        }
    }
}

@Composable
private fun ProductSaleDetailsContent(
    modifier: Modifier = Modifier,
    state: PurchaseDetailsUiState,
    isAdmin: Boolean = false,
    onAction: (SaleDetailsUiAction) -> Unit,
) {
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductSaleDetailsTopBar(
                receiptNumber = state.purchase.receiptNumber,
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
                receiptNumber = state.purchase.receiptNumber,
                createdAt = state.purchase.createdAt,
                totalPrice = state.purchase.totalAmount,
                paymentStatus = state.purchase.paymentStatus,
                receiptStatus = null,
                isPurchase = true,
                onRequestReceipt = {
                    onAction(SaleDetailsUiAction.OnRequestReceipt)
                }
            )
            if (state.supplier.id.isNotBlank()) {
                // customer info
                CustomerInfoSection(
                    isPurchase = true,
                    customerName = state.purchase.customerName,
                    customerDebt = state.supplier.moneyDebtAmount,
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
                    products = state.purchase.products,
                    isModifyEnabled = false,
                    productType = state.purchase.productType
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
            if (isAdmin && state.purchase.logs.isNotEmpty()){
                TitledSection(
                    title = stringResource(R.string.change_logs)
                ) {
                    ChangeLogList(
                        changeLogs = state.purchase.logs
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
