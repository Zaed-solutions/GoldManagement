package com.zaed.manager.ui.salescheques.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.PaymentTypes
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.SaveChequePaymentBottomSheetContent
import com.zaed.common.ui.components.SelectCustomerContent
import com.zaed.common.ui.components.getContainerColor
import com.zaed.common.ui.components.getContentColor
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.manager.R
import com.zaed.manager.ui.salescheques.SalesChequesUiAction
import com.zaed.manager.ui.salescheques.SalesChequesUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesChequesScreenContent(
    modifier: Modifier = Modifier,
    uiState: SalesChequesUiState,
    onShowNavDrawer: () -> Unit,
    onAddNewCustomer: () -> Unit = {},
    query: String = "",
    onQueryChanged: (String) -> Unit = {},
    selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    onCustomerSelected: (WholeSaleCustomer) -> Unit = {},
    suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    onAddPayment: (Payment) -> Unit = {},
    onEditPayment: (Payment, Payment) -> Unit = { _, _ -> },
    onAction: (SalesChequesUiAction) -> Unit = {}
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePaymentSheet by remember { mutableStateOf(false) }
    var selectPaymentTypeSheet by remember { mutableStateOf(false) }
    var selectCustomerSheet by remember { mutableStateOf(false) }
    var isTaken by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { 2 }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.d("TAG", "CustomerDetailsScreenContent: ${uiState.selectedCustomer.debtAmount}")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = uiState.selectedCustomer.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = Ellipsis
                )
            }, navigationIcon = {
                IconButton(onShowNavDrawer) {
                    Icon(
                        imageVector = Icons.Default.Menu, contentDescription = ""
                    )
                }
            }
            )
        }, modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)

        ) {
            val tabs = remember {
                listOf(
                    context.getString(R.string.sales_cheques),
                    context.getString(R.string.manager_cheques)
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.difference),
                                    style = MaterialTheme.typography.titleLarge,

                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    color = uiState.payments.sumOf { it.signedAmount() }.getContainerColor(),
                                    shape = RoundedCornerShape(8f.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        text = uiState.payments.sumOf { it.signedAmount() }.toMoneyFormat(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = uiState.payments.sumOf { it.signedAmount() }.getContentColor(),
                                    )
                                }
                            }
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
                    1 -> {}
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
                    PaymentType.CHEQUE -> {
                        selectedPayment?.let {
                            SaveChequePaymentBottomSheetContent(initialPayment = selectedPayment as ChequePayment,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAddPayment(
                                            updatedPayment.copy(
//                                                customerId = selectedCustomer.id,
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
                    else -> {}
                }

            }
        }

        AnimatedVisibility(selectPaymentTypeSheet) {
            ModalBottomSheet(onDismissRequest = {
                selectedPayment = null
                selectPaymentTypeSheet = false
            }) {
                PaymentTypes(types = listOf(PaymentType.CHEQUE), onPaymentTypeSelected = { type ->
                    when (type) {

                        PaymentType.CHEQUE -> {
                            selectedPayment = ChequePayment(type = type)
                            selectPaymentTypeSheet = false
                            selectCustomerSheet = true
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
                selectedPayment?.let { onAction(SalesChequesUiAction.DeletePayment(it)) }
                confirmDeletePaymentSheet = false
                selectedPayment = null
            })
        AnimatedVisibility(selectCustomerSheet) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                onDismissRequest = {
                    selectCustomerSheet = false
                },
                dragHandle = {}
            ) {
                SelectCustomerContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = TopAppBarDefaults.TopAppBarExpandedHeight),
                    onAddNewCustomer = onAddNewCustomer,
                    query = query,
                    onQueryChanged = onQueryChanged,
                    selectedCustomer = selectedCustomer,
                    onCustomerSelected = {selected->
                        onCustomerSelected(selected)
                        selectCustomerSheet = false
                        addPaymentBottomSheetVisible = true
                    },
                    suggestedCustomers = suggestedCustomers
                )
            }
        }
    }
}