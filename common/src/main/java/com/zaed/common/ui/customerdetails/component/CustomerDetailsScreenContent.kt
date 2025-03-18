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
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.BalanceSection
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.PaymentTypes
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.TransactionsList
import com.zaed.common.ui.components.SaveBankTransferPaymentBottomSheetContent
import com.zaed.common.ui.components.SaveCashPaymentBottomSheetContent
import com.zaed.common.ui.components.SaveChequePaymentBottomSheetContent
import com.zaed.common.ui.components.SaveFuturePaymentBottomSheetContent
import com.zaed.common.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.common.ui.customerdetails.CustomerDetailsUiState
import com.zaed.common.ui.util.toMoneyFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState,
    selectedCustomer: WholeSaleCustomer,
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
    val pagerState = rememberPagerState { 2 }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.d("TAG", "CustomerDetailsScreenContent: ${uiState.customer.debtAmount}")
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
                    context.getString(com.zaed.common.R.string.payments),
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
                                amount = uiState.customer.debtAmount,
                            )
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.payments,
                                onRemovePayment = { payment ->
                                    selectedPayment = payment
                                    confirmDeletePaymentSheet = true
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

                    1 -> {
                        TransactionsList (listState = listState,
                            isLoading = uiState.loading,
                            transactions = uiState.sales,
                            onDeleteTransaction = { sale, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnDeleteProductSale(sale.id)
                                    } else {
                                        CustomerDetailsUiAction.OnDeleteGoldSale(sale.id)
                                    }
                                )
                            },
                            onEditTransaction = { sale, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnEditProductSale(sale.id)
                                    } else {
                                        CustomerDetailsUiAction.OnEditGoldSale(sale.id)
                                    }
                                )
                            },
                            onTransactionClicked = { sale, isProduct ->
                                onAction(
                                    if (isProduct) {
                                        CustomerDetailsUiAction.OnProductSaleClicked(sale.id)
                                    } else {
                                        CustomerDetailsUiAction.OnGoldSaleClicked(sale.id)
                                    }
                                )
                            })
                    }
                }
            }
        }
        AnimatedVisibility(addPaymentBottomSheetVisible) {
            Log.d("TAG", "CustomerDetailsScreenContent: $selectedPayment")
            ModalBottomSheet(
                onDismissRequest = {
                    selectedPayment = null
                    addPaymentBottomSheetVisible = false
                },
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
                                                amount = updatedPayment.amount,
                                                given = !isTaken
                                            )
                                        )

                                    } else {
                                        onEditPayment(
                                            selectedPayment as CashPayment,
                                            updatedPayment
                                        )
                                    }
                                    selectedPayment = null
                                    addPaymentBottomSheetVisible = false
                                })
                        }
                    }

                    PaymentType.CHEQUE -> {
                        selectedPayment?.let {
                            SaveChequePaymentBottomSheetContent(initialPayment = selectedPayment as ChequePayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = updatedPayment.amount,
                                                given = !isTaken
                                            )
                                        )
                                    } else {
                                        onEditPayment(
                                            selectedPayment as ChequePayment,
                                            updatedPayment
                                        )
                                    }
                                    selectedPayment = null
                                    addPaymentBottomSheetVisible = false
                                })
                        }
                    }

                    PaymentType.BANK_TRANSFER -> {
                        selectedPayment?.let {
                            SaveBankTransferPaymentBottomSheetContent(initialPayment = selectedPayment as BankTransferPayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = updatedPayment.amount,
                                                given = !isTaken
                                            )
                                        )
                                    } else {
                                        onEditPayment(
                                            selectedPayment as BankTransferPayment,
                                            updatedPayment
                                        )
                                    }
                                    selectedPayment = null
                                    addPaymentBottomSheetVisible = false
                                })
                        }
                    }

                    PaymentType.FUTURES -> {
                        selectedPayment?.let {
                            SaveFuturePaymentBottomSheetContent(
                                initialPayment = selectedPayment as FuturePayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
                                                customerId = selectedCustomer.id,
                                                amount = updatedPayment.amount,
                                                given = !isTaken
                                            )
                                        )
                                    } else {
                                        onEditPayment(
                                            selectedPayment as FuturePayment,
                                            updatedPayment
                                        )
                                    }
                                    selectedPayment = null
                                    addPaymentBottomSheetVisible = false
                                })
                        }
                    }


                    else -> {}
                }

            }
        }

        AnimatedVisibility(selectPaymentTypeSheet) {
            ModalBottomSheet(onDismissRequest = {
                selectedPayment = null
                selectPaymentTypeSheet = false
            }) {
                PaymentTypes(types = getProductSalePayments(), onPaymentTypeSelected = { type ->
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