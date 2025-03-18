package com.zaed.common.ui.saledetails.cashiersaledetails.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.components.ChangeLogList
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.CustomerInfoSection
import com.zaed.common.ui.components.MultiOptionSwitch
import com.zaed.common.ui.components.PhoneNumberTextField
import com.zaed.common.ui.components.PrinterBottomSheet
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsUiState
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import com.zaed.common.ui.saledetails.productsaledetails.components.ProductSaleDetailsTopBar
import com.zaed.common.ui.saledetails.productsaledetails.components.SaleInfoSection
import com.zaed.common.ui.util.isValidPhoneNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: SaleDetailsUiState,
    isAdmin: Boolean = false,
    onAction: (SaleDetailsUiAction) -> Unit
) {
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    var isPrinterSheetVisible by remember { mutableStateOf(false) }
    var isShareBottomSheetIsVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        isShareBottomSheetIsVisible = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.share))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        isPrinterSheetVisible = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.print))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null
                    )
                }
            }
            //products
            TitledSection(
                title = stringResource(R.string.products)
            ) {
                ProductsTable(
                    products = state.storeSale.products,
                    isModifyEnabled = false,
                    productType = ProductType.PRODUCT
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
            PrinterBottomSheet(
                isVisible = isPrinterSheetVisible,
                onDismiss = { isPrinterSheetVisible = false },
                sale = state.storeSale
            )
            AnimatedVisibility(isShareBottomSheetIsVisible) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    ),
                    onDismissRequest = { isShareBottomSheetIsVisible = false },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var emailFieldVisible by remember { mutableStateOf(false) }
                        var phoneFieldVisible by remember { mutableStateOf(true) }
                        var emailError by remember { mutableStateOf(false) }
                        var phoneError by remember { mutableStateOf(false) }
                        Text(
                            text = stringResource(R.string.share_receipt_via),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)

                        )
                        MultiOptionSwitch(
                            modifier = Modifier.heightIn(max = 48.dp),
                            options = listOf(
                                R.string.whatsapp,
                                R.string.email
                            ).map { stringResource(it) },
                            selectedIndex = if (emailFieldVisible) 1 else 0,
                            onOptionSelected = {
                                when (it) {
                                    0 -> {
                                        emailFieldVisible = false
                                        phoneFieldVisible = true
                                    }

                                    1 -> {
                                        phoneFieldVisible = false
                                        emailFieldVisible = true
                                    }
                                }
                            }
                        )
                        AnimatedContent(emailFieldVisible to phoneFieldVisible) { contentState ->
                            when {
                                contentState.first -> {
                                    TextInputTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.storeSale.customerEmail,
                                        label = stringResource(R.string.email),
                                        onValueChange = {
                                            onAction(SaleDetailsUiAction.UpdateCustomerEmail(it))
                                        },
                                        imageVector = Icons.Default.Email,
                                        isError = emailError,
                                        errorMessage = R.string.not_a_valid_email
                                    )
                                }

                                contentState.second -> {
                                    PhoneNumberTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.storeSale.customerPhone,
                                        onValueChange = {
                                            onAction(
                                                SaleDetailsUiAction.UpdateCustomerPhoneNumber(
                                                    it
                                                )
                                            )
                                        },
                                        isError = phoneError,
                                        errorMessage = R.string.not_a_valid_phone_number
                                    )
                                }
                            }

                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .heightIn(min = 48.dp),
                            onClick = {
                                if (phoneFieldVisible) {
                                    if (state.storeSale.customerPhone.isValidPhoneNumber()) {
                                        phoneError = false
                                        onAction(SaleDetailsUiAction.ShareViaWhatsapp(state.storeSale))
                                    } else {
                                        phoneError = true
                                    }
                                } else {
                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(state.storeSale.customerEmail)
                                            .matches()
                                    ) {
                                        emailError = false
                                        onAction(SaleDetailsUiAction.ShareViaEmail(state.storeSale))
                                    } else {
                                        emailError = true
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.share)
                            )
                        }

                    }
                }
            }
        }
    }
}