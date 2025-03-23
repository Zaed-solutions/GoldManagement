package com.zaed.common.ui.salescheques.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.material3.TopAppBar
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
import com.zaed.common.R
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.SavePaymentBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.components.SelectCustomerContent
import com.zaed.common.ui.components.SelectFromSalesChequesBottomSheetContent
import com.zaed.common.ui.components.getContainerColor
import com.zaed.common.ui.components.getContentColor
import com.zaed.common.ui.salescheques.SalesChequesUiAction
import com.zaed.common.ui.salescheques.SalesChequesUiState
import com.zaed.common.ui.suppliers.SelectSupplierSheet
import com.zaed.common.ui.util.toMoneyFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesChequesScreenContent(
    modifier: Modifier = Modifier,
    uiState: SalesChequesUiState,
    onAction: (SalesChequesUiAction) -> Unit = {},
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePaymentSheet by remember { mutableStateOf(false) }
    var isSelectAccountTypeSheetVisible by remember { mutableStateOf(false) }
    var selectCustomerSheet by remember { mutableStateOf(false) }
    var selectSupplierSheet by remember { mutableStateOf(false) }
    var isSelectSalesChequeSheetVisible by remember { mutableStateOf(false) }
    var isTaken by remember { mutableStateOf(false) }
    var isSupplier by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { 2 }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.d("TAG", "CustomerDetailsScreenContent: ${uiState.selectedAccount.moneyDebtAmount}")
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.cheques),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = Ellipsis
                )
            }, navigationIcon = {
                IconButton(
                    onClick = {
                        onAction(SalesChequesUiAction.OnDrawerClicked)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu, contentDescription = ""
                    )
                }
            }
            )
        }, modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {
            val tabs = remember {
                listOf(
                    context.getString(R.string.sales_cheques),
                    context.getString(R.string.manager_cheques)
                )
            }
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                query = uiState.chequeSearchQuery,
                placeHolder = stringResource(R.string.search_by_customer_supplier_name),
                onQueryChanged = {
                    onAction(SalesChequesUiAction.OnUpdateChequeSearchQuery(it))
                }
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val selected = uiState.selectedChequeFilter == null
                    FilterChip(
                        selected = selected,
                        onClick = {
                            onAction(SalesChequesUiAction.OnChequeFilterSelected(null))
                        },
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        label = {
                            Text(
                                text = stringResource(R.string.all)
                            )
                        }
                    )
                }
                items(ChequeStatus.entries) { filter ->
                    val selected = filter == uiState.selectedChequeFilter
                    FilterChip(
                        selected = selected,
                        onClick = {
                            onAction(SalesChequesUiAction.OnChequeFilterSelected(filter))
                        },
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        label = {
                            Text(
                                text = stringResource(filter.titleRes)
                            )
                        }
                    )
                }
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
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.difference),
                                    style = MaterialTheme.typography.titleLarge,

                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                val differenceAmount = remember(uiState.filteredSalesCheques) {
                                    uiState.filteredSalesCheques.sumOf { it.signedAmount() }
                                }
                                Surface(
                                    color = differenceAmount.getContainerColor(),
                                    shape = RoundedCornerShape(8f.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        text = differenceAmount.toMoneyFormat(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = differenceAmount.getContentColor(),
                                    )
                                }
                            }
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.filteredSalesCheques,
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
                                        isSelectAccountTypeSheetVisible = true
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
                                        isSelectAccountTypeSheetVisible = true
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

                    1 -> {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.difference),
                                    style = MaterialTheme.typography.titleLarge,

                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                val differenceAmount = remember(uiState.allManagerCheques) {
                                    uiState.allManagerCheques.sumOf { it.signedAmount() }
                                }
                                Surface(
                                    color = differenceAmount.getContainerColor(),
                                    shape = RoundedCornerShape(8f.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        text = differenceAmount.toMoneyFormat(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = differenceAmount.getContentColor(),
                                    )
                                }
                            }
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.filteredManagerCheques,
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
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    onClick = {
                                        isTaken = false
                                        isSupplier = true
                                        selectSupplierSheet = true
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
        }
        SelectAccountTypeBottomSheet(
            isVisible = isSelectAccountTypeSheetVisible,
            onDismiss = {
                isSelectAccountTypeSheetVisible = false
            },
            onAccountTypeSelected = { supplier ->
                isSelectAccountTypeSheetVisible = false
                if (supplier) {
                    isSupplier = true
                    selectSupplierSheet = true
                } else {
                    isSupplier = false
                    selectCustomerSheet = true
                }
            }
        )
        SavePaymentBottomSheet(
            isVisible = addPaymentBottomSheetVisible,
            onDismiss = {
                addPaymentBottomSheetVisible = false
                selectedPayment = null
            },
            initialPayment = selectedPayment ?: ChequePayment(),
            isTaken = isTaken,
            selectedAccount = uiState.selectedAccount,
            currentUser = uiState.currentUser,
            onSave = { updatedPayment ->
                addPaymentBottomSheetVisible = false
                if (selectedPayment?.id.isNullOrBlank()) {
                    onAction(
                        SalesChequesUiAction.OnAddPayment(
                            isSupplier = isSupplier,
                            payment = updatedPayment
                        )
                    )
                } else {
                    onAction(
                        SalesChequesUiAction.OnEditPayment(
                            oldPayment = selectedPayment ?: ChequePayment(),
                            newPayment = updatedPayment,
                            isSupplier = isSupplier
                        )
                    )
                }
                selectedPayment = null
            }
        )

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
                    onAddNewCustomer = {
                        onAction(SalesChequesUiAction.OnAddNewCustomer)
                    },
                    query = uiState.customerSearchQuery,
                    onQueryChanged = {
                        onAction(SalesChequesUiAction.OnCustomerSearchQueryChanged(it))
                    },
                    selectedCustomer = WholeSaleCustomer(),
                    onCustomerSelected = { selected ->
                        onAction(SalesChequesUiAction.OnAccountSelected(selected))
                        selectCustomerSheet = false
                        selectedPayment = ChequePayment(
                            customerId = selected.id,
                            given = !isTaken,
                        )
                        if (isTaken) {
                            addPaymentBottomSheetVisible = true
                        } else {
                            isSelectSalesChequeSheetVisible = true
                        }
                    },
                    suggestedCustomers = uiState.suggestedCustomers
                )
            }
        }
        AnimatedVisibility(selectSupplierSheet) {
            SelectSupplierSheet(
                onDismiss = {
                    selectSupplierSheet = false
                },
                isAdmin = uiState.isAdmin,
                isLoading = uiState.loading,
                onUpdateSearchQuery = {
                    onAction(
                        SalesChequesUiAction.OnUpdateSupplierSearchQuery(it)
                    )
                },
                searchQuery = uiState.searchQuery,
                filteredSuppliers = uiState.filteredSuppliers,
                onSupplierClicked = { supplier ->
                    onAction(SalesChequesUiAction.OnAccountSelected(supplier))
                    selectSupplierSheet = false
                    if (pagerState.currentPage == 0) {
                        selectedPayment = ChequePayment(
                            customerId = supplier.id,
                            given = !isTaken,
                        )
                        if (isTaken) {
                            addPaymentBottomSheetVisible = true
                        } else {
                            isSelectSalesChequeSheetVisible = true
                        }
                    } else {
                        selectedPayment = ManagerCheque(
                            customerId = supplier.id,
                            given = !isTaken,
                        )
                        addPaymentBottomSheetVisible = true
                    }
                },
                onAddSupplier = {
                    onAction(SalesChequesUiAction.OnAddSupplier(it))
                }
            )
        }
        AnimatedVisibility(isSelectSalesChequeSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    isSelectSalesChequeSheetVisible = false
                },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
            ) {
                SelectFromSalesChequesBottomSheetContent(
                    salesCheques = uiState.uncashedSalesCheques,
                    selectedChequesPayment = emptyList(),
                    onSelect = {
                        isSelectSalesChequeSheetVisible = false
                        onAction(SalesChequesUiAction.OnTransferCheque(
                            cheque = it,
                            isSupplier = isSupplier
                        )
                        )
                    },
                    onRemove = { },
                    onDismiss = {
                        isSelectSalesChequeSheetVisible = false
                    },
                    remainsAmount = Double.MAX_VALUE
                )
            }
        }
    }
}
