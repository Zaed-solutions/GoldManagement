package com.zaed.common.ui.supplierdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getSupplierPaymentTypes
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.BalanceSection
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PaymentTypes
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.SavePaymentBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.components.TransactionsList
import com.zaed.common.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.common.ui.suppliers.components.SaveSupplierBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SupplierDetailsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    supplierId: String,
    onNavigateToPurchaseDetails: (String) -> Unit,
    onNavigateToEditPurchase: (String) -> Unit,
    viewModel: SupplierDetailsViewModel = koinViewModel()
) {
    LaunchedEffect(true) {
        viewModel.init(supplierId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SupplierDetailsScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                SupplierDetailsUiAction.OnBackClicked -> onBackPressed()
                is SupplierDetailsUiAction.OnEditPurchase -> onNavigateToEditPurchase(action.purchaseId)
                is SupplierDetailsUiAction.OnPurchaseClicked -> onNavigateToPurchaseDetails(action.purchaseId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupplierDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: SupplierDetailsUiState,
    onAction: (SupplierDetailsUiAction) -> Unit
) {
    val pagerState = rememberPagerState { SupplierDetailsTabs.entries.size }
    val scope = rememberCoroutineScope()
    var isEditSupplierSheetVisible by remember {
        mutableStateOf(false)
    }
    var selectedPayment: Payment by remember {
        mutableStateOf(CashPayment())
    }
    var isPurchase by remember {
        mutableStateOf(false)
    }
    var selectedPurchaseId by remember {
        mutableStateOf("")
    }
    var isConfirmDeletePaymentSheetVisible by remember {
        mutableStateOf(false)
    }
    var isSavePaymentSheetVisible by remember {
        mutableStateOf(false)
    }
    var isTaken by remember {
        mutableStateOf(false)
    }
    var isSelectPaymentTypeSheetVisible by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.supplier.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                },
                navigationIcon = {
                    BackIcon {
                        onAction(SupplierDetailsUiAction.OnBackClicked)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isEditSupplierSheetVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            //supplier details
            DetailRow(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                label = stringResource(R.string.phone_number),
                value = state.supplier.phone
            )
            DetailRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = stringResource(R.string.email),
                value = state.supplier.email
            )
            //tab row
            PrimaryTabRow(
                modifier = Modifier.padding(top = 16.dp),
                selectedTabIndex = pagerState.currentPage, indicator = {
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
                SupplierDetailsTabs.entries.forEach { tab ->
                    Tab(
                        selected = pagerState.currentPage == tab.ordinal,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(tab.ordinal)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(tab.titleRes)
                            )
                        }
                    )
                }
            }
            //horizontal pager
            HorizontalPager(
                modifier = Modifier.padding(top = 16.dp),
                state = pagerState,
                userScrollEnabled = false,
            ) { value ->
                when (value) {
                    SupplierDetailsTabs.PURCHASES.ordinal -> {
                        Column {
                            SearchBar(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                query = state.purchasesSearchQuery,
                                onQueryChanged = {
                                    onAction(SupplierDetailsUiAction.UpdatePurchasesSearchQuery(it))
                                },
                            )
                            TransactionsList(
                                isLoading = false,
                                transactions = state.filteredPurchases,
                                onTransactionClicked = { purchase, _ ->
                                    onAction(SupplierDetailsUiAction.OnPurchaseClicked(purchase.id))
                                },
                                onEditTransaction = { purchase, _ ->
                                    onAction(SupplierDetailsUiAction.OnEditPurchase(purchase.id))
                                },
                                onDeleteTransaction = { purchase, _ ->
                                    isPurchase = true
                                    selectedPurchaseId = purchase.id
                                    isConfirmDeletePaymentSheetVisible = true
                                }
                            )
                        }
                    }

                    SupplierDetailsTabs.PAYMENTS.ordinal -> {
                        //payments
                        Column {
                            BalanceSection(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                isSupplier = true,
                                amount = state.supplier.moneyDebtAmount,
                            )
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = state.payments,
                                onRemovePayment = { payment ->
                                    selectedPayment = payment
                                    isPurchase = false
                                    isConfirmDeletePaymentSheetVisible = true
                                },
                                canCashed = state.currentUser.role != UserRole.DISTRIBUTOR,
                                onChequeCashed = {payment->
                                    onAction(SupplierDetailsUiAction.UpdatePayment(payment,(payment as ChequePayment).copy(chequeStatus = ChequeStatus.CASHED)))
                                },
                                onEditPayment = { payment ->
                                    selectedPayment = payment
                                    isSavePaymentSheetVisible = true
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
                                        isSelectPaymentTypeSheetVisible = true
                                    }, colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ), shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(text = stringResource(R.string.taken))
                                }
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    onClick = {
                                        isTaken = false
                                        isSelectPaymentTypeSheetVisible = true
                                    },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text(text = stringResource(R.string.given))
                                }
                            }
                        }
                    }
                }
            }
            SaveSupplierBottomSheet(
                isVisible = isEditSupplierSheetVisible,
                onDismiss = {
                    isEditSupplierSheetVisible = false
                },
                onSave = {
                    isEditSupplierSheetVisible = false
                    onAction(SupplierDetailsUiAction.UpdateSupplier(it))
                },
                initialSupplier = state.supplier
            )
            SavePaymentBottomSheet(
                isVisible = isSavePaymentSheetVisible,
                onDismiss = {
                    isSavePaymentSheetVisible = false
                    selectedPayment = CashPayment()
                },
                initialPayment = selectedPayment?: ChequePayment(),
                isTaken = isTaken,
                selectedAccount = state.supplier,
                currentUser = state.currentUser,
                onSave =  { updatedPayment ->
                    isSavePaymentSheetVisible = false
                    if (selectedPayment.id.isBlank()) {
                        onAction(
                            SupplierDetailsUiAction.AddPayment(
                                updatedPayment
                            )
                        )
                    } else {
                        onAction(
                            SupplierDetailsUiAction.UpdatePayment(
                                selectedPayment,
                                updatedPayment
                            )
                        )
                    }
                }
            )
            AnimatedVisibility(isSelectPaymentTypeSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isSelectPaymentTypeSheetVisible = false
                    }
                ) {
                    PaymentTypes(
                        selectedAccount = state.supplier,
                        types = getSupplierPaymentTypes(given = !isTaken),
                        onPaymentTypeSelected = { type ->
                            when (type) {
                                PaymentType.CASH -> {
                                    selectedPayment = CashPayment(type = type)
                                    isSelectPaymentTypeSheetVisible = false
                                    isSavePaymentSheetVisible = true
                                }

                                PaymentType.BANK_TRANSFER -> {
                                    selectedPayment = BankTransferPayment(type = type)
                                    isSelectPaymentTypeSheetVisible = false
                                    isSavePaymentSheetVisible = true
                                }

                                PaymentType.CHEQUE -> {
                                    selectedPayment = ChequePayment(type = type)
                                    isSelectPaymentTypeSheetVisible = false
                                    isSavePaymentSheetVisible = true
                                }

                                PaymentType.MANAGER_CHEQUES -> {
                                    selectedPayment = ManagerCheque(type = type)
                                    isSelectPaymentTypeSheetVisible = false
                                    isSavePaymentSheetVisible = true
                                }

                                else -> {
                                    isSelectPaymentTypeSheetVisible = false
                                }
                            }
                        }
                    )
                }
            }
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeletePaymentSheetVisible,
                label = if(isPurchase) stringResource(R.string.purchase) else stringResource(R.string.payment),
                onDismiss = {
                    isConfirmDeletePaymentSheetVisible = false
                },
                onConfirm = {
                    isConfirmDeletePaymentSheetVisible = false
                    onAction(
                        if (isPurchase)
                            SupplierDetailsUiAction.DeletePurchase(selectedPurchaseId)
                        else
                            SupplierDetailsUiAction.DeletePayment(selectedPayment)
                    )
                }
            )
        }
    }
}

private enum class SupplierDetailsTabs(val titleRes: Int) {
    PURCHASES(R.string.purchases),
    PAYMENTS(R.string.payments),
}