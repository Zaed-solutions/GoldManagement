package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID
import kotlin.math.absoluteValue

@Composable
fun SaveCashPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialPayment: CashPayment,
    remainsAmount: Double = 0.0,
    onSave: (CashPayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = " طريقه الدفع : ${stringResource(initialPayment.type.titleRes)}",
            style = MaterialTheme.typography.titleLarge
        )

        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.amount),
            value = payment.amount,
            onValueChange = {
                payment = payment.copy(amount = it)
            },
        )
        if (payment.amount > remainsAmount) {
            Text(
                text = "Remains for the client " + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (payment.amount > remainsAmount) {
                    payment = payment.copy(amount = remainsAmount)
                } else {
                    payment = payment.copy(
                        id = "distributor-" + UUID.randomUUID().toString()
                    )
                }
                onSave(payment)
            },
            enabled = payment.amount != 0.0
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SaveChequePaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    remainsAmount: Double = 0.0,
    initialPayment: ChequePayment,
    onSave: (ChequePayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = " طريقه الدفع : ${stringResource(initialPayment.type.titleRes)}",
            style = MaterialTheme.typography.titleLarge
        )
        //cheque for
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.cheque_for),
            value = payment.chequeFor,
            onValueChange = {
                payment = payment.copy(chequeFor = it)
            },
        )
        //receiver Name
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.receiver_name),
            value = payment.receiverName,
            onValueChange = {
                payment = payment.copy(receiverName = it)
            },
        )
        //City
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.city),
            value = payment.city,
            onValueChange = {
                payment = payment.copy(city = it)
            },
        )
        //Sender Name
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.sender_name),
            value = payment.senderName,
            onValueChange = {
                payment = payment.copy(senderName = it)
            },
        )
        //VALUE
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.amount),
            value = payment.amount,
            onValueChange = {
                payment = payment.copy(amount = it)
            },
        )
        if (payment.amount > remainsAmount) {
            Text(
                text = "Remains for the client " + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            )
        }
        //Note
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.note),
            value = payment.notes,
            onValueChange = {
                payment = payment.copy(notes = it)
            },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (payment.amount > remainsAmount) {
                    payment = payment.copy(amount = remainsAmount)
                } else {
                    payment = payment.copy(
                        id = "distributor-" + UUID.randomUUID().toString()
                    )
                }
                onSave(payment)
            },
            enabled = payment.amount != 0.0
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SaveBankTransferPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    remainsAmount: Double = 0.0,
    initialPayment: BankTransferPayment,
    onSave: (BankTransferPayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = " طريقه الدفع : ${stringResource(initialPayment.type.titleRes)}",
            style = MaterialTheme.typography.titleLarge
        )
        //Account holder name
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.account_holder_name),
            value = payment.accountHolderName,
            onValueChange = {
                payment = payment.copy(accountHolderName = it)
            },
        )
        //Account Number
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.account_number),
            value = payment.accountNumber,
            onValueChange = {
                payment = payment.copy(accountNumber = it)
            },
        )
        //BANK NAME
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(com.zaed.common.R.string.bank_Name),
            value = payment.bankName,
            onValueChange = {
                payment = payment.copy(bankName = it)
            },
        )
        //VALUE
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.amount),
            value = payment.amount,
            onValueChange = {
                payment = payment.copy(amount = it)
            },
        )
        if (payment.amount > remainsAmount) {
            Text(
                text = "Remains for the client " + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (payment.amount > remainsAmount) {
                    payment = payment.copy(amount = remainsAmount)
                } else {
                    payment = payment.copy(
                        id = "distributor-" + UUID.randomUUID().toString()
                    )
                }
                onSave(payment)
            },
            enabled = payment.amount != 0.0
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

