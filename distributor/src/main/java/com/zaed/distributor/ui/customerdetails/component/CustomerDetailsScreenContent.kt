package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.addproductsale.components.PaymentTypes
import com.zaed.distributor.ui.addproductsale.components.SaveBankTransferPaymentBottomSheetContent
import com.zaed.distributor.ui.addproductsale.components.SaveCashPaymentBottomSheetContent
import com.zaed.distributor.ui.addproductsale.components.SaveChequePaymentBottomSheetContent
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiState
import com.zaed.distributor.ui.sales.components.SalesList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState,
    selectedCustomer: WholeSaleCustomer,
    onAddPayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {},
    onAction: (CustomerDetailsUiAction) -> Unit = {}
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    val listState = remember { LazyListState() }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePaymentSheet by remember { mutableStateOf(false) }
    var editPaymentSheet by remember { mutableStateOf(false) }
    var selectPaymentTypeSheet by remember { mutableStateOf(false) }
    var isTaken by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { 2 }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.customer.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                },
                navigationIcon = {
                    BackIcon {
                        onAction(CustomerDetailsUiAction.OnBackClicked)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAction(CustomerDetailsUiAction.OnEditCustomer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(it)

        ) {
            val tabs = remember {
                listOf(
                    context.getString(com.zaed.common.R.string.payments),
                    context.getString(com.zaed.common.R.string.sales)
                )
            }
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .run {
                                if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                                    scale(-1f, 1f)
                                else
                                    this
                            }
                            .tabIndicatorOffset(pagerState.currentPage, true),
                        width = Dp.Unspecified,
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { value ->
                when (value) {
                    0 -> {
                        Column {
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                listState = listState,
                                debtAmount = uiState.customer.debtAmount,
                                payments = uiState.payments,
                                onDeletePayment = { payment ->
                                    selectedPayment = payment
                                    confirmDeletePaymentSheet = true
                                },
                                onEditPayment = { payment ->
                                    onAction(CustomerDetailsUiAction.EditPayment(payment))
                                    editPaymentSheet = true
                                }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    onClick = {
                                        isTaken = true
                                        selectPaymentTypeSheet = true
                                    },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(text = "أخذت")
                                }
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    onClick = {
                                        isTaken = false
                                        selectPaymentTypeSheet = true
                                    },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(text = "أعطيت")
                                }
                            }
                        }
                    }

                    1 -> {
                        SalesList(
                            listState = listState,
                            isLoading = uiState.loading,
                            sales = uiState.sales,
                            onDeleteSale = { saleId, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnDeleteProductSale(saleId)
                                    } else {
                                        CustomerDetailsUiAction.OnDeleteGoldSale(saleId)
                                    }
                                )
                            },
                            onEditSale = { saleId, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnEditProductSale(saleId)
                                    } else {
                                        CustomerDetailsUiAction.OnEditGoldSale(saleId)
                                    }
                                )
                            },
                            onSaleClicked = { saleId, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnProductSaleClicked(saleId)
                                    } else {
                                        CustomerDetailsUiAction.OnGoldSaleClicked(saleId)
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
        AnimatedVisibility(addPaymentBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { addPaymentBottomSheetVisible = false },
            ) {
                when (selectedPayment?.type) {
                    PaymentType.CASH -> {
                        selectedPayment?.let {
                            SaveCashPaymentBottomSheetContent(
                                initialPayment = selectedPayment as CashPayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = if (isTaken) updatedPayment.amount else updatedPayment.amount.unaryMinus()
                                            )
                                        )
                                    } else {
                                        onEditPayment(updatedPayment)
                                    }
                                    addPaymentBottomSheetVisible = false
                                }
                            )
                        }
                    }

                    PaymentType.CHEQUE -> {
                        selectedPayment?.let {
                            SaveChequePaymentBottomSheetContent(
                                initialPayment = selectedPayment as ChequePayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = if (isTaken) updatedPayment.amount else updatedPayment.amount.unaryMinus()

                                            )
                                        )
                                    } else {
                                        onEditPayment(updatedPayment)
                                    }
                                    addPaymentBottomSheetVisible = false
                                }
                            )
                        }
                    }

                    PaymentType.BANK_TRANSFER -> {
                        selectedPayment?.let {
                            SaveBankTransferPaymentBottomSheetContent(
                                initialPayment = selectedPayment as BankTransferPayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = if (isTaken) updatedPayment.amount else updatedPayment.amount.unaryMinus()
                                            )
                                        )
                                    } else {
                                        onEditPayment(updatedPayment)
                                    }
                                    addPaymentBottomSheetVisible = false
                                }
                            )
                        }
                    }

                    else -> {}
                }

            }
        }

        AnimatedVisibility(selectPaymentTypeSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedPayment = null
                    selectPaymentTypeSheet = false
                }
            ) {
                PaymentTypes(
                    types = getProductSalePayments(),
                    onPaymentTypeSelected = { type ->
                        when (type) {
                            PaymentType.CASH -> {
                                selectedPayment = CashPayment(type = type)
                                selectPaymentTypeSheet = false
                                addPaymentBottomSheetVisible = true
                            }

                            PaymentType.BANK_TRANSFER -> {
                                selectedPayment = BankTransferPayment(type = type)
                                selectPaymentTypeSheet = false
                                addPaymentBottomSheetVisible = true
                            }

                            PaymentType.CHEQUE -> {
                                selectedPayment = ChequePayment(type = type)
                                selectPaymentTypeSheet = false
                                addPaymentBottomSheetVisible = true
                            }

                            else -> {
                                selectedPayment = null
                                selectPaymentTypeSheet = false
                            }
                        }
                    }
                )
            }
        }
        ConfirmDeleteBottomSheet(
            visible = confirmDeletePaymentSheet,
            label = selectedPayment?.amount?.toMoneyFormat(2) ?: "",
            onDismiss = {
                confirmDeletePaymentSheet = false
                selectedPayment = null
            },
            onConfirm = {
                selectedPayment?.let { onAction(CustomerDetailsUiAction.DeletePayment(it)) }
                confirmDeletePaymentSheet = false
                selectedPayment = null
            }
        )
    }
}