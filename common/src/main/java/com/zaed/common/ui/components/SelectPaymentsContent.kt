package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.suppliers.SelectSupplierSheet
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentsContent(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    payments: List<Payment>,
    products : List<Product> = emptyList(),
    payWithGold : Boolean = false,
    onAddPayment: (Payment) -> Unit = {},
    onRemovePayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {},
    onAddNewCustomer: () -> Unit = {},
    currentUser: User,
    query: String,
    paymentsTypes: List<PaymentType> = remember { getProductSalePayments() },
    onQueryChanged: (String) -> Unit,
    selectedAccount: Account,
    onAccountSelected: (Account) -> Unit,
    suggestedAccount: List<Account>,
    onNext: () -> Unit = {},
    isPurchase: Boolean = false,
    isAdmin: Boolean = false,
    isLoading: Boolean = false,
    supplierSearchQuery: String = "",
    onUpdateSupplierSearchQuery: (String) -> Unit = {},
    filteredSuppliers: List<Supplier> = emptyList(),
    onSupplierClicked: (String) -> Unit = {},
    onAddSupplier: (Supplier) -> Unit = {},
    totalPaid: Double,
    salesCheques: List<ChequePayment> = emptyList(),
) {
    var showSupplierSheet by remember { mutableStateOf(false) }
    var simplePaymentBottomSheet by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var warningNotFullyPaidSheet by remember { mutableStateOf(false) }
    var selectCustomerSheet by remember { mutableStateOf(false) }
    var confirmTheRemainsSheetVisible by remember { mutableStateOf(false) }
    val remainsAmount by rememberUpdatedState(
        if(!payWithGold) totalAmount - totalPaid
        else products.sumOf { it.laborCost } - payments.sumOf { it.amount }
    )
    val remainsGold by rememberUpdatedState(
        products.sumOf { it.grams } - (payments.filterIsInstance<GoldPayment>().sumOf { it.givenGoldAmount })
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.the_amount_to_be_paid),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = if(!payWithGold) {totalAmount.toMoneyFormat(2) } else {
                stringResource(
                    R.string.grams_placeholder,products.sumOf { it.grams })
            },
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        if (totalPaid > 0 || payments.filterIsInstance<GoldPayment>().any { it.givenGoldAmount>0 }) {
            Text(
                text = stringResource(R.string.remaining_amount_from, totalAmount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = if(!payWithGold)remainsAmount.toMoneyFormat(2) else "$remainsGold g" ,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = stringResource(R.string.select_payment_method),
            style = MaterialTheme.typography.headlineSmall
        )
        PaymentTypes(
            types = paymentsTypes,
            onPaymentTypeSelected = { type ->
                when (type) {
                    PaymentType.CASH -> {
                        selectedPayment = CashPayment(
                            type = type,
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.BANK_TRANSFER -> {
                        selectedPayment = BankTransferPayment(
                            type = type,
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.CHEQUE -> {
                        selectedPayment = ChequePayment(
                            type = type,
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.GOLD -> {
                        selectedPayment = GoldPayment(
                            type = type,
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.MANAGER_CHEQUES -> {
                        selectedPayment = ManagerCheque(
                            type = type,
                            id = "Destributor-" + UUID.randomUUID().toString()
                        )
                        simplePaymentBottomSheet = true
                    }

                    else -> {}
                }
            }
        )
        PaymentsList(
            modifier = Modifier.weight(1f),
            payments = payments,
            onEditPayment = {
                selectedPayment = it
                simplePaymentBottomSheet = true
            },
            onRemovePayment = onRemovePayment
        )


        Button(
            onClick = {
                if (payments.filterIsInstance<GoldPayment>()
                        .any { it.pricePerGram == 0.0 } && selectedAccount.id.isBlank()
                ) {
                    if (selectedAccount is WholeSaleCustomer) {
                        selectCustomerSheet = true
                    } else {
                        showSupplierSheet = true
                    }
                } else if (remainsAmount > 0 && payments.filterIsInstance<GoldPayment>()
                        .none { it.pricePerGram == 0.0 }
                ) {
                    warningNotFullyPaidSheet = true
                } else if (remainsAmount < 0 && payments.filterIsInstance<GoldPayment>()
                        .none { it.pricePerGram == 0.0 }
                ) {
                    confirmTheRemainsSheetVisible = true

                } else {
                    onNext()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(
                text = stringResource(R.string.continue_word),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        androidx.compose.animation.AnimatedVisibility(confirmTheRemainsSheetVisible) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = {
                    confirmTheRemainsSheetVisible = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.the_payment_amount_is_greater_than_the_total_amount),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = if (selectedAccount is WholeSaleCustomer) stringResource(R.string.do_you_want_to_make_is_balance_for_the_customer) else stringResource(
                            R.string.do_you_want_to_make_is_balance_for_you
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            if (selectedAccount.id.isNotBlank()) {
                                if (selectedAccount is WholeSaleCustomer) {
                                    onAddPayment(
                                        FuturePayment(
                                            accountId = selectedAccount.id,
                                            amount = remainsAmount.absoluteValue,
                                            given = false,
                                            type = PaymentType.REMAIN
                                        )
                                    )
                                } else {
                                    onAddPayment(
                                        FuturePayment(
                                            accountId = selectedAccount.id,
                                            amount = remainsAmount.absoluteValue,
                                            given = true,
                                            type = PaymentType.REMAIN
                                        )
                                    )
                                }
                            } else if (selectedAccount is WholeSaleCustomer) {
                                selectCustomerSheet = true
                            } else {
                                showSupplierSheet = true
                            }
                            confirmTheRemainsSheetVisible = false
                        },
                    ) {
                        Text(
                            text = if (!isPurchase) stringResource(R.string.balance_for_customer) else stringResource(
                                R.string.balance_for_you
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        AnimatedVisibility(showSupplierSheet) {
            SelectSupplierSheet(
                onDismiss = {
                    showSupplierSheet = false
                },
                isAdmin = isAdmin,
                isLoading = isLoading,
                onUpdateSearchQuery = { onUpdateSupplierSearchQuery(it) },
                searchQuery = supplierSearchQuery,
                filteredSuppliers = filteredSuppliers,
                onSupplierClicked = { supplier ->
                    onSupplierClicked(supplier.id)
                    showSupplierSheet = false
                },
                onAddSupplier = {
                    onAddSupplier(it)
                }
            )
        }
        //add payment bottom sheet
        selectedPayment?.let {
            SavePaymentBottomSheet(
                isVisible = simplePaymentBottomSheet,
                onDismiss = {
                    simplePaymentBottomSheet = false
                    selectedPayment = null
                },
                initialPayment = it,
                isTaken = !isPurchase,
                selectedAccount = selectedAccount,
                currentUser = currentUser,
                onSave = { updatedPayment ->
                    if (selectedPayment!!.id.isBlank()) {
                        onAddPayment(updatedPayment)
                    } else {
                        onEditPayment(updatedPayment)
                    }
                    simplePaymentBottomSheet = false
                    selectedPayment = null
                }
            )
        }
        AnimatedVisibility(
            visible = warningNotFullyPaidSheet,
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    warningNotFullyPaidSheet = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.the_invoice_amount_has_not_been_paid_in_full),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = if (selectedAccount is WholeSaleCustomer) stringResource(R.string.do_you_want_to_record_it_on_the_customer_or_register_it_as_a_sales_loss) else stringResource(
                            R.string.do_you_want_to_register_it_as_a_debt
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!isPurchase) {
                            OutlinedButton(
                                shape = RoundedCornerShape(4.dp),
                                onClick = {
                                    onAddPayment(
                                        LossPayment(
                                            accountId = selectedAccount.id,
                                            amount = remainsAmount,
                                            given = true,
                                            type = PaymentType.LOSS
                                        )
                                    )
                                    warningNotFullyPaidSheet = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.register_as_a_sales_loss),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Button(
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                if (selectedAccount.id.isNotBlank()) {
                                    onAddPayment(
                                        FuturePayment(
                                            accountId = selectedAccount.id,
                                            amount = remainsAmount,
                                            given = true,
                                            type = PaymentType.FUTURES
                                        )
                                    )
                                } else if (selectedAccount is WholeSaleCustomer) {
                                    selectCustomerSheet = true
                                } else {
                                    showSupplierSheet = true
                                }
                                warningNotFullyPaidSheet = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (!isPurchase) stringResource(R.string.record_on_the_customer) else stringResource(
                                    R.string.record_as_debt
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                }
            }
        }
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
                    selectedCustomer = selectedAccount as WholeSaleCustomer,
                    onCustomerSelected = {
                        onAccountSelected(it)
                        selectCustomerSheet = false
                    },
                    suggestedCustomers = suggestedAccount as List<WholeSaleCustomer>
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFromSalesChequesBottomSheetContent(
    salesCheques: List<ChequePayment>,
    selectedChequesPayment: List<ChequePayment>,
    onSelect: (ChequePayment) -> Unit,
    onRemove: (ChequePayment) -> Unit = {},
    onDismiss: () -> Unit = {},
    remainsAmount: Double
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = {},
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.select_cheques))
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(salesCheques) { cheque ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (selectedChequesPayment.contains(cheque)) {
                                onRemove(cheque)
                            } else {
                                onSelect(cheque)
                            }
                        },
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                    selected = true,
                                    onClick = {},
                                    label = { Text(text = stringResource(cheque.type.titleRes)) },

                                    )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = cheque.senderName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                RadioButton(
                                    selected = selectedChequesPayment.contains(cheque),
                                    onClick = {
                                        if (selectedChequesPayment.contains(cheque)) {
                                            onRemove(cheque)
                                        } else {
                                            onSelect(cheque)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cheque.createdAt.format(DateFormat.TIME),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = cheque.amount.toMoneyFormat(2),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                            }
                        }
                    }
                    HorizontalDivider()
                }

            }
            Button(
                onClick = onDismiss,
                enabled = selectedChequesPayment.sumOf { it.amount } <= remainsAmount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (selectedChequesPayment.sumOf { it.amount } <= remainsAmount) {
                        if (remainsAmount < Double.MAX_VALUE) {
                            Text(
                                selectedChequesPayment.sumOf { it.amount }.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Text(
                            text = stringResource(R.string.select_cheques),
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.selected_cheques_amount_is_greater_than_the_remaining_amount),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
private fun SelectPaymentsContentPreview() {
    SelectPaymentsContent(
        totalAmount = 1000.0,
        payments = listOf(
            ChequePayment(
                id = "1",
                amount = 50.0,
            ),
            CashPayment(
                id = "2",
                amount = 500.0,
            ),
            BankTransferPayment(
                id = "3",
                amount = 500.0,
            )
        ),
        selectedAccount = WholeSaleCustomer(),
        onAddPayment = {},
        onRemovePayment = {},
        onEditPayment = {},
        onNext = {},
        onAddNewCustomer = {},
        query = "",
        onQueryChanged = {},
        onAccountSelected = {},
        currentUser = User(),
        suggestedAccount = listOf(),
        totalPaid = 20.0,
    )

}


