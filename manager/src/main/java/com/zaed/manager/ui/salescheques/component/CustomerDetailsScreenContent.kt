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
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.PaymentsList
import com.zaed.common.ui.components.SaveChequePaymentBottomSheetContent
import com.zaed.common.ui.components.SaveManagerChequePaymentBottomSheetContent
import com.zaed.common.ui.components.SelectCustomerContent
import com.zaed.common.ui.components.getContainerColor
import com.zaed.common.ui.components.getContentColor
import com.zaed.common.ui.suppliers.SelectSupplierSheet
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
    query: String = "",
    selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    onAction: (SalesChequesUiAction) -> Unit = {},
    isAdmin: Boolean = false,
    isLoading: Boolean = false,

    searchQuery: String = "",
    filteredSuppliers: List<Supplier>,
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePaymentSheet by remember { mutableStateOf(false) }
    var selectCustomerSheet by remember { mutableStateOf(false) }
    var selectSupplierSheet by remember { mutableStateOf(false) }
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
                                Surface(
                                    color = uiState.salesPayments.sumOf { it.signedAmount() }
                                        .getContainerColor(),
                                    shape = RoundedCornerShape(8f.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        text = uiState.salesPayments.sumOf { it.signedAmount() }
                                            .toMoneyFormat(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = uiState.salesPayments.sumOf { it.signedAmount() }
                                            .getContentColor(),
                                    )
                                }
                            }
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.salesPayments,
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
                                        selectCustomerSheet = true
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
                                        selectCustomerSheet = true
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
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding( 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.difference),
                                    style = MaterialTheme.typography.titleLarge,

                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    color = uiState.managerPayments.sumOf { it.signedAmount() }
                                        .getContainerColor(),
                                    shape = RoundedCornerShape(8f.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        text = uiState.managerPayments.sumOf { it.signedAmount() }
                                            .toMoneyFormat(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = uiState.managerPayments.sumOf { it.signedAmount() }
                                            .getContentColor(),
                                    )
                                }
                            }
                            PaymentsList(
                                modifier = Modifier.weight(1f),
                                payments = uiState.managerPayments,
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
                                        selectSupplierSheet = true
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
                                        selectSupplierSheet = true
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
                                        onAction(
                                            SalesChequesUiAction.OnAddPayment(
                                                updatedPayment.copy(
                                                    amount = updatedPayment.amount,
                                                    given = !isTaken
                                                )
                                            )
                                        )
                                    } else {
                                        onAction(
                                            SalesChequesUiAction.OnEditPayment(
                                                selectedPayment as ChequePayment,
                                                updatedPayment
                                            )
                                        )
                                    }
                                    selectedPayment = null
                                    addPaymentBottomSheetVisible = false
                                })
                        }
                    }

                    PaymentType.MANAGER_CHEQUES -> {
                        selectedPayment?.let {
                            SaveManagerChequePaymentBottomSheetContent(initialPayment = selectedPayment as ManagerCheque,
                                remainsAmount = Double.MAX_VALUE,
                                onSave = { updatedPayment ->
                                    if (selectedPayment!!.id.isBlank()) {
                                        onAction(
                                            SalesChequesUiAction.OnAddPayment(
                                            updatedPayment.copy(
                                                amount = updatedPayment.amount,
                                                given = !isTaken
                                            )
                                        )
                                        )
                                    } else {
                                        onAction(
                                            SalesChequesUiAction.OnEditPayment(                                            selectedPayment as ManagerCheque,
                                            updatedPayment
                                        )
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
                    query = query,
                    onQueryChanged = {
                        onAction(SalesChequesUiAction.OnQueryChanged(it))
                    },
                    selectedCustomer = selectedCustomer,
                    onCustomerSelected = { selected ->
                        onAction(SalesChequesUiAction.OnCustomerSelected(selected))
                        selectCustomerSheet = false
                        selectedPayment = ChequePayment(
                            customerId = selected.id,
                            given = !isTaken,
                        )
                        addPaymentBottomSheetVisible = true
                    },
                    suggestedCustomers = suggestedCustomers
                )
            }
        }
        AnimatedVisibility(selectSupplierSheet) {
            SelectSupplierSheet(
                onDismiss = {
                    selectSupplierSheet = false
                },
                isAdmin = isAdmin,
                isLoading = isLoading,
                onUpdateSearchQuery = {
                    onAction(
                        SalesChequesUiAction.OnUpdateSearchQuery(it)
                    )
                },
                searchQuery = searchQuery,
                filteredSuppliers = filteredSuppliers,
                onSupplierClicked = { supplierId ->
                    onAction(SalesChequesUiAction.OnSupplierClicked(supplierId))
                    selectSupplierSheet = false
                    selectedPayment = ManagerCheque(
                        customerId = supplierId,
                        given = !isTaken,
                    )
                    addPaymentBottomSheetVisible = true
                },
                onAddSupplier = {
                    onAction(SalesChequesUiAction.OnAddSupplier(it))
                }
            )
        }
    }
}