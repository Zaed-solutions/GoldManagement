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
import com.zaed.common.data.model.cheque.ChequeType
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.ui.util.toMoneyFormat
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
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
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
            supportingText =
            if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount + initialPayment.amount).absoluteValue.toMoneyFormat(
                    2
                )
            } else {
                ""
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                    payment = payment.copy(
                        amount = remainsAmount,
                    )
                } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                    payment = payment.copy(
                        amount = remainsAmount + initialPayment.amount,
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
fun SaveFuturePaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialPayment: FuturePayment,
    remainsAmount: Double = 0.0,
    onSave: (FuturePayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
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
            supportingText =
            if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount + initialPayment.amount).absoluteValue.toMoneyFormat(
                    2
                )
            } else {
                ""
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                    payment = payment.copy(
                        amount = remainsAmount,
                    )
                } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                    payment = payment.copy(
                        amount = remainsAmount + initialPayment.amount,
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
fun SaveGoldPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialPayment: GoldPayment,
    remainsAmount: Double = 0.0,
    onSave: (GoldPayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
        )

        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.grams),
            value = payment.givenGoldAmount,
            onValueChange = {
                payment = payment.copy(givenGoldAmount = it)
            },
            supportingText =
            if(((payment.givenGoldAmount * payment.pricePerGram) > (remainsAmount+(initialPayment.amount)))){
                stringResource(R.string.the_value_is_bigger_than_the_remains_amount)
            }else{
                ""
            }
        )
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.gram_price),
            value = payment.pricePerGram,
            onValueChange = {
                payment = payment.copy(pricePerGram = it)
            },
            supportingText =
            if(((payment.givenGoldAmount * payment.pricePerGram) > (remainsAmount+(initialPayment.amount)))){
                stringResource(R.string.the_value_is_bigger_than_the_remains_amount)
            }else{
                ""
            }
        )
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.karat),
            value = payment.givenGoldKarat.toDouble(),
            onValueChange = {
                payment = payment.copy(givenGoldKarat = it.toInt())
            },
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                onSave(payment.copy(amount = payment.givenGoldAmount * payment.pricePerGram))
            },
            enabled = payment.givenGoldAmount != 0.0 &&((payment.givenGoldAmount * payment.pricePerGram) <= remainsAmount+(initialPayment.amount))
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
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
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
            supportingText =
            if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount + initialPayment.amount).absoluteValue.toMoneyFormat(
                    2
                )
            } else {
                ""
            }
        )

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
                if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                    payment = payment.copy(
                        amount = remainsAmount,
                    )
                } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                    payment = payment.copy(
                        amount = remainsAmount + initialPayment.amount,
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
fun SaveManagerChequePaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    remainsAmount: Double = 0.0,
    initialPayment: ManagerCheque,
    onSave: (ManagerCheque) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
        )
        //cheque for
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.cheque_number),
            value = payment.chequeNumber,
            onValueChange = {
                payment = payment.copy(chequeNumber = it)
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
        TitledDropDownTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.cheque_type),
            selectedValue = stringResource(payment.chequeType.titleRes),
            onValueChanged = {
                payment = payment.copy(
                    chequeType = ChequeType.entries[it],
                )
            },
            options = ChequeType.entries.map { stringResource(it.titleRes) }
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
            supportingText =
            if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount + initialPayment.amount).absoluteValue.toMoneyFormat(
                    2
                )
            } else {
                ""
            }
        )

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
                if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                    payment = payment.copy(
                        amount = remainsAmount,
                    )
                } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                    payment = payment.copy(
                        amount = remainsAmount + initialPayment.amount,
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
            text = stringResource(R.string.payment_method) +stringResource(initialPayment.type.titleRes),
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
            supportingText =
            if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount).absoluteValue.toMoneyFormat(
                    2
                )
            } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                stringResource(R.string.remains_for_the_client) + payment.amount.minus(remainsAmount + initialPayment.amount).absoluteValue.toMoneyFormat(
                    2
                )
            } else {
                ""
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if (initialPayment.amount == 0.0 && payment.amount > remainsAmount) {
                    payment = payment.copy(
                        amount = remainsAmount,
                    )
                } else if (initialPayment.amount != 0.0 && payment.amount > (remainsAmount + initialPayment.amount)) {
                    payment = payment.copy(
                        amount = remainsAmount + initialPayment.amount,
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

