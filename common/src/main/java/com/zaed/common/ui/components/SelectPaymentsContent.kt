package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    selectedCustomer: WholeSaleCustomer,
    onCustomerSelected: (WholeSaleCustomer) -> Unit,
    suggestedCustomers: List<WholeSaleCustomer>,
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
            text = "المبلغ الواجب دفعه",
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
                text = "المبلغ المتبقي من ${totalAmount}",
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
            text = "حدد طريقة الدفع",
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
                if( payments.filterIsInstance<GoldPayment>().any { it.pricePerGram == 0.0 } && selectedCustomer.id.isBlank()){
                    selectCustomerSheet = true
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
                "متابعة",
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
                                    if (selectedPayment!!.id.startsWith("Destributor")) {
                                        onAddPayment(
                                            newPayment.copy(
                                                id = "Payment-" + UUID.randomUUID().toString(),
                                                customerId = selectedCustomer.id,
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
                                                customerId = selectedCustomer.id,
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
                                                customerId = selectedCustomer.id,
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
                                                customerId = selectedCustomer.id,
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
                        text = "لم يتم تسديد قيمه الفاتوره بالكامل",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "هل تريد تسجيلها على العميل ام تسجيلها كخسارة مبيعات",
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
                                        customerId = selectedCustomer?.id ?: "",
                                        amount = remainsAmount,
                                        type = PaymentType.LOSS
                                    )
                                )
                                warningnNotFullyPaidSheet = false
                            },
                        ) {
                            Text(text = "تسجيل كخسارة مبيعات")
                        }
                        Button(
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                if (selectedCustomer?.id?.isNotBlank() == true) {
                                    onAddPayment(
                                        FuturePayment(
                                            customerId = selectedCustomer?.id ?: "",
                                            amount = remainsAmount,
                                            type = PaymentType.FUTURES
                                        )
                                    )
                                } else {
                                    selectCustomerSheet = true
                                }
                                warningnNotFullyPaidSheet = false

                            }
                        ) {
                            Text(text = "تسجيل على العميل")
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
                    selectedCustomer = selectedCustomer,
                    onCustomerSelected = {
                        onCustomerSelected(it)
                        selectCustomerSheet = false
                    },
                    suggestedCustomers = suggestedCustomers
                )
            }
        }

    }
}

@Composable
fun PaymentTypes(
    types: List<PaymentType> = getProductSalePayments(),
    onPaymentTypeSelected: (PaymentType) -> Unit = {}
) {
    LazyColumn {
        items(types) { paymentType ->
            Surface(
                onClick = { onPaymentTypeSelected(paymentType) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            painter = painterResource(paymentType.iconRes),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(paymentType.titleRes),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
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
        selectedCustomer = WholeSaleCustomer(),
        onAddPayment = {},
        onRemovePayment = {},
        onEditPayment = {},
        onNext = {},
        onAddNewCustomer = {},
        query = "",
        onQueryChanged = {},
        onCustomerSelected = {},
        suggestedCustomers = listOf(),
    )

}


