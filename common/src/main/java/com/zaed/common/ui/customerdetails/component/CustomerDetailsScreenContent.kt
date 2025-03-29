package com.zaed.common.ui.customerdetails.component

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.BalanceSection
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.PaymentTypes
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.SavePaymentBottomSheet
import com.zaed.common.ui.components.TransactionsList
import com.zaed.common.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.common.ui.customerdetails.CustomerDetailsUiState
import com.zaed.common.ui.util.toMoneyFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState,
    onAddPayment: (Payment) -> Unit = {},
    onEditPayment: (Payment, Payment) -> Unit = { _, _ -> },
    onAction: (CustomerDetailsUiAction) -> Unit = {}
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    val listState = remember { LazyListState() }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePaymentSheet by remember { mutableStateOf(false) }
    var selectPaymentTypeSheet by remember { mutableStateOf(false) }
    var isTaken by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { 3 }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.d("TAG", "CustomerDetailsScreenContent: ${uiState.customer.moneyDebtAmount}")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = uiState.customer.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = Ellipsis
                )
            }, navigationIcon = {
                BackIcon {
                    onAction(CustomerDetailsUiAction.OnBackClicked)
                }
            }, actions = {
                IconButton(onClick = {
                    onAction(CustomerDetailsUiAction.OnEditCustomer)
                }) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = ""
                    )
                }
            })
        }, modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)

        ) {
            val tabs = remember {
                listOf(
                    context.getString(R.string.money_payment),
                    context.getString(R.string.gold_payment),
                    context.getString(com.zaed.common.R.string.sales)
                )
            }
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage, indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier
                        .run {
                            if (LocalLayoutDirection.current == LayoutDirection.Rtl) scale(
                                -1f, 1f
                            )
                            else this
                        }
                        .tabIndicatorOffset(pagerState.currentPage, true),
                    width = Dp.Unspecified,
                )
            }) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = { Text(text = title) })
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { value ->
                when (value) {
                    0 -> {
                        Column {
                            BalanceSection(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                amount = uiState.customer.moneyDebtAmount,
                            )
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.payments,
                                onRemovePayment = { payment ->
                                    selectedPayment = payment
                                    confirmDeletePaymentSheet = true
                                },
                                canCashed = uiState.currentDistributor.role !=UserRole.DISTRIBUTOR,
                                onChequeCashed = {payment->
                                    onAction(CustomerDetailsUiAction.EditPayment((payment as ChequePayment).copy(chequeStatus = ChequeStatus.CASHED)))
                                },
                                onEditPayment = { payment ->
                                    selectedPayment = payment
                                    Log.d("TAG", "CustomerDetailsScreenContent2: $selectedPayment")
                                    addPaymentBottomSheetVisible = true
                                })
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp), onClick = {
                                        isTaken = true
                                        selectPaymentTypeSheet = true
                                    }, colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ), shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(text = stringResource(com.zaed.common.R.string.taken))
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
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text(text = stringResource(com.zaed.common.R.string.given))
                                }
                            }
                        }
                    }
                    1->{}

                    2 -> {
                        TransactionsList(listState = listState,
                            isLoading = uiState.loading,
                            transactions = uiState.sales,
                            onDeleteTransaction = { sale, productType ->
                                onAction(
                                    when (productType) {
                                        ProductType.PRODUCT -> CustomerDetailsUiAction.OnDeleteProductSale(
                                            sale.id
                                        )
                                        else -> {
                                            CustomerDetailsUiAction.OnDeleteGoldSale(sale.id)
                                        }
                                    }
                                )
                            },
                            onEditTransaction = { sale, productType ->
                                onAction(
                                    when (productType) {
                                        ProductType.PRODUCT -> CustomerDetailsUiAction.OnEditProductSale(
                                            sale.id
                                        )
                                        else-> {
                                            CustomerDetailsUiAction.OnEditGoldSale(sale.id)
                                        }
                                    }
                                )
                            },
                            onTransactionClicked = { sale, productType ->
                                onAction(
                                    when (productType) {
                                        ProductType.PRODUCT -> CustomerDetailsUiAction.OnProductSaleClicked(
                                            sale.id
                                        )
                                        else-> {
                                            CustomerDetailsUiAction.OnGoldSaleClicked(sale.id)
                                        }
                                    }
                                )
                            })
                    }
                }
            }
        }
        SavePaymentBottomSheet(
            isVisible = addPaymentBottomSheetVisible,
            onDismiss = {
                addPaymentBottomSheetVisible = false
                selectedPayment = null
            },
            initialPayment = selectedPayment ?: ChequePayment(),
            isTaken = isTaken,
            currentUser = uiState.currentDistributor,
            selectedAccount = uiState.customer,
            onSave = { updatedPayment ->
                if (selectedPayment!!.id.isBlank()) {
                    onAddPayment(
                        updatedPayment
                    )
                } else {
                    onEditPayment(
                        selectedPayment ?: ChequePayment(),
                        updatedPayment
                    )
                }
                addPaymentBottomSheetVisible = false
                selectedPayment = null
            }
        )
        AnimatedVisibility(selectPaymentTypeSheet) {
            ModalBottomSheet(onDismissRequest = {
                selectedPayment = null
                selectPaymentTypeSheet = false
            }) {
                PaymentTypes(
                    selectedAccount = uiState.customer,
                    types = getProductSalePayments(), onPaymentTypeSelected = { type ->
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
                })
            }
        }
        ConfirmDeleteBottomSheet(visible = confirmDeletePaymentSheet,
            label = selectedPayment?.amount?.toMoneyFormat(2) ?: "",
            onDismiss = {
                confirmDeletePaymentSheet = false
                selectedPayment = null
            },
            onConfirm = {
                selectedPayment?.let { onAction(CustomerDetailsUiAction.DeletePayment(it)) }
                confirmDeletePaymentSheet = false
                selectedPayment = null
            })
    }
}