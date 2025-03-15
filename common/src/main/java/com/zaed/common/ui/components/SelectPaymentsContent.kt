package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentsContent(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    payments: List<Payment>,
    onAddPayment: (Payment) -> Unit = {},
    onRemovePayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {},
    onAddNewCustomer: () -> Unit,
    query: String,
    paymentsTypes: List<PaymentType> = remember { getProductSalePayments() },
    onQueryChanged: (String) -> Unit,
    selectedAccount: Account,
    onAccountSelected: (Account) -> Unit,
    suggestedAccount: List<Account>,
    onNext: () -> Unit = {}
) {
    var simplePaymentBottomSheet by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    val simplePaymentBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var warningnNotFullyPaidSheet by remember { mutableStateOf(false) }
    var selectCustomerSheet by remember { mutableStateOf(false) }
    val totalPaid by rememberUpdatedState(payments.sumOf { it.amount })
    val remainsAmount by rememberUpdatedState(totalAmount - totalPaid)
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
            text = totalAmount.toMoneyFormat(2),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        if (totalPaid > 0) {
            Text(
                text = stringResource(R.string.remaining_amount_from, totalAmount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = remainsAmount.toMoneyFormat(2),
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
                            id = "Destributor-" + UUID.randomUUID().toString()
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.BANK_TRANSFER -> {
                        selectedPayment = BankTransferPayment(
                            type = type,
                            id = "Destributor-" + UUID.randomUUID().toString()
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.CHEQUE -> {
                        selectedPayment = ChequePayment(
                            type = type,
                            id = "Destributor-" + UUID.randomUUID().toString()
                        )
                        simplePaymentBottomSheet = true
                    }

                    PaymentType.GOLD -> {
                        selectedPayment = GoldPayment(
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
                if( payments.filterIsInstance<GoldPayment>().any { it.pricePerGram == 0.0 } && selectedAccount.id.isBlank()){
                    if(selectedAccount is WholeSaleCustomer){
                        selectCustomerSheet = true
                    }
                }
                else if (remainsAmount > 0 && payments.filterIsInstance<GoldPayment>().none { it.pricePerGram == 0.0 }) {
                    warningnNotFullyPaidSheet = true
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
        //add payment bottom sheet
        AnimatedVisibility(simplePaymentBottomSheet) {
            ModalBottomSheet(
                sheetState = simplePaymentBottomSheetState,
                onDismissRequest = { simplePaymentBottomSheet = false },
            ) {
                when (selectedPayment?.type) {
                    PaymentType.CASH -> {
                        selectedPayment?.let {
                            SaveCashPaymentBottomSheetContent(
                                initialPayment = selectedPayment as CashPayment,
                                remainsAmount = remainsAmount,
                                onSave = { newPayment ->
                                    val cashPayment = newPayment as CashPayment
                                    if (selectedPayment!!.id.startsWith("Destributor")) {
                                        onAddPayment(
                                            cashPayment.copy(
                                                id = "Payment-" + UUID.randomUUID().toString(),
                                                customerId = selectedAccount.id,
                                            )
                                        )
                                    } else {
                                        onEditPayment(cashPayment)
                                    }
                                    simplePaymentBottomSheet = false
                                }
                            )
                        }
                    }

                    PaymentType.GOLD -> {
                        selectedPayment?.let {
                            SaveGoldPaymentBottomSheetContent(
                                initialPayment = selectedPayment as GoldPayment,
                                remainsAmount = remainsAmount,
                                onSave = { newPayment ->
                                    if (selectedPayment!!.id.startsWith("Destributor")) {
                                        onAddPayment(
                                            newPayment.copy(
                                                id = "Payment-" + UUID.randomUUID().toString(),
                                                customerId = selectedAccount.id,
                                            )
                                        )
                                    } else {
                                        onEditPayment(newPayment)
                                    }
                                    simplePaymentBottomSheet = false
                                }
                            )
                        }
                    }

                    PaymentType.CHEQUE -> {
                        selectedPayment?.let {
                            SaveChequePaymentBottomSheetContent(
                                initialPayment = selectedPayment as ChequePayment,
                                remainsAmount = remainsAmount,
                                onSave = { newPayment ->
                                    if (selectedPayment!!.id.startsWith("Destributor")) {
                                        onAddPayment(
                                            newPayment.copy(
                                                id = "Payment-" + UUID.randomUUID().toString(),
                                                customerId = selectedAccount.id,
                                            )
                                        )
                                    } else {
                                        onEditPayment(newPayment)
                                    }
                                    simplePaymentBottomSheet = false
                                }
                            )
                        }
                    }

                    PaymentType.BANK_TRANSFER -> {
                        selectedPayment?.let {
                            SaveBankTransferPaymentBottomSheetContent(
                                initialPayment = selectedPayment as BankTransferPayment,
                                remainsAmount = remainsAmount,
                                onSave = { newPayment ->
                                    if (selectedPayment!!.id.startsWith("Destributor")) {
                                        onAddPayment(
                                            newPayment.copy(
                                                id = "Payment-" + UUID.randomUUID().toString(),
                                                customerId = selectedAccount.id,
                                            )
                                        )
                                    } else {
                                        onEditPayment(newPayment)
                                    }
                                    simplePaymentBottomSheet = false
                                }
                            )
                        }
                    }

                    else -> {}
                }

            }
        }
        AnimatedVisibility(
            visible = warningnNotFullyPaidSheet,
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    warningnNotFullyPaidSheet = false
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
                        text = stringResource(R.string.do_you_want_to_record_it_on_the_customer_or_register_it_as_a_sales_loss),
                                style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                onAddPayment(
                                    LossPayment(
                                        customerId = selectedAccount.id,
                                        amount = remainsAmount,
                                        given = true,
                                        type = PaymentType.LOSS
                                    )
                                )
                                warningnNotFullyPaidSheet = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.register_as_a_sales_loss),
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                if (selectedAccount.id.isNotBlank()) {
                                    onAddPayment(
                                        FuturePayment(
                                            customerId = selectedAccount.id,
                                            amount = remainsAmount,
                                            given = true,
                                            type = PaymentType.FUTURES
                                        )
                                    )
                                } else {
                                    selectCustomerSheet = true
                                }
                                warningnNotFullyPaidSheet = false

                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.record_on_the_customer),
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
        suggestedAccount = listOf(),
    )

}


