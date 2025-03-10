package com.zaed.common.ui.saledetails.cashiersaledetails.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.components.ChangeLogList
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.CustomerInfoSection
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsUiState
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import com.zaed.common.ui.saledetails.productsaledetails.components.ProductSaleDetailsTopBar
import com.zaed.common.ui.saledetails.productsaledetails.components.SaleInfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: SaleDetailsUiState,
    isAdmin: Boolean = false,
    onAction: (SaleDetailsUiAction) -> Unit
) {
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    BackHandler {
        onAction(SaleDetailsUiAction.OnBackClicked)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProductSaleDetailsTopBar(
                receiptNumber = state.storeSale.receiptNumber,
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
                receiptNumber = state.storeSale.receiptNumber,
                createdAt = state.storeSale.createdAt,
                totalPrice = state.storeSale.totalAmount,

                onRequestReceipt = {
                    onAction(SaleDetailsUiAction.OnRequestReceipt)
                }
            )
            if (state.storeSale.customerId.isNotBlank()) {
                // customer info
                CustomerInfoSection(
                    customerName = state.storeSale.customerName,
                    customerDebt = 0.0,
                    onCustomerClicked = {}
                )
            }
            //products
            TitledSection(
                title = stringResource(R.string.products)
            ) {
                ProductsTable(
                    products = state.storeSale.products,
                    isModifyEnabled = false
                )
            }
            if (isAdmin && state.storeSale.logs.isNotEmpty()){
                TitledSection(
                    title = stringResource(R.string.change_logs)
                ) {
                    ChangeLogList(
                        changeLogs = state.storeSale.logs
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