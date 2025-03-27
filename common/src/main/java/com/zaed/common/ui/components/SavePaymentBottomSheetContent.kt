package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.cheque.ChequeType
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePaymentBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    initialPayment: Payment,
    selectedAccount: Account,
    currentUser: User,
    isTaken: Boolean,
    onSave: (Payment) -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible
    ) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            ),
            onDismissRequest = onDismiss,
        ) {
            when (initialPayment.type) {
                PaymentType.CHEQUE -> {
                    SaveChequePaymentBottomSheetContent(
                        initialPayment = initialPayment as ChequePayment,
                        isTaken = isTaken,
                        selectedAccount = selectedAccount,
                        currentUser = currentUser,
                        onSave = onSave
                    )
                }

                PaymentType.MANAGER_CHEQUES -> {
                    SaveManagerChequePaymentBottomSheetContent(
                        initialPayment = initialPayment as ManagerCheque,
                        isTaken = isTaken,
                        currentUser = currentUser,
                        selectedAccount = selectedAccount,
                        onSave = onSave
                    )
                }

                PaymentType.CASH -> {
                    SaveCashPaymentBottomSheetContent(
                        initialPayment = initialPayment as CashPayment,
                        isTaken = isTaken,
                        selectedAccount = selectedAccount,
                        onSave = onSave
                    )
                }

                PaymentType.BANK_TRANSFER -> {
                    SaveBankTransferPaymentBottomSheetContent(
                        initialPayment = initialPayment as BankTransferPayment,
                        isTaken = isTaken,
                        selectedAccount = selectedAccount,
                        onSave = onSave
                    )
                }

                PaymentType.FUTURES -> {
                    SaveFuturePaymentBottomSheetContent(
                        initialPayment = initialPayment as FuturePayment,
                        isTaken = isTaken,
                        selectedAccount = selectedAccount,
                        onSave = onSave
                    )
                }

                PaymentType.GOLD -> {
                    SaveGoldPaymentBottomSheetContent(
                        initialPayment = initialPayment as GoldPayment,
                        isTaken = isTaken,
                        selectedAccount = selectedAccount,
                        onSave = onSave
                    )
                }

                else -> {}
            }

        }
    }
}

@Composable
fun SaveCashPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialPayment: CashPayment,
    isTaken: Boolean,
    selectedAccount: Account,
    onSave: (CashPayment) -> Unit = {}
) {
    var payment by remember { mutableStateOf(initialPayment) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
        )
        if(selectedAccount.id.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

                ) {
                    Text(
                        text = selectedAccount.name.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = selectedAccount.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            HorizontalDivider()
        }
        NumberInputTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = stringResource(R.string.amount),
            value = payment.amount,
            onValueChange = {
                payment = payment.copy(amount = it)
            },
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
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
    isTaken: Boolean,
    selectedAccount: Account,
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
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
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
    isTaken: Boolean,
    selectedAccount: Account,
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
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
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
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
                    )
                }
                onSave(payment.copy(amount = payment.givenGoldAmount * payment.pricePerGram))
            },
            enabled = payment.givenGoldAmount != 0.0
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
    initialPayment: ChequePayment,
    isTaken: Boolean,
    currentUser: User,
    selectedAccount: Account,
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
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
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
            supportingText = if(selectedAccount.note.isNotBlank()) stringResource(R.string.note_template, selectedAccount.note) else ""
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        receiverName = currentUser.fullName,
                        receiverId = currentUser.id,
                        senderName = selectedAccount.name,
                        senderId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
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
    initialPayment: ManagerCheque,
    isTaken: Boolean,
    currentUser: User,
    selectedAccount: Account,
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
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
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
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        senderName = currentUser.fullName,
                        senderId = currentUser.id,
                        receiverName = selectedAccount.name,
                        receiverId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
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
    initialPayment: BankTransferPayment,
    isTaken: Boolean,
    selectedAccount: Account,
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
            text = stringResource(R.string.payment_method) + stringResource(initialPayment.type.titleRes),
            style = MaterialTheme.typography.titleLarge
        )
        //Account holder name
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(R.string.account_holder_name),
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
            label = stringResource(R.string.account_number),
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
            label = stringResource(R.string.bank_Name),
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(top = 24.dp),
            onClick = {
                if(payment.id.isBlank()){
                    payment = payment.copy(
                        given = !isTaken,
                        accountId = selectedAccount.id,
                        id = "Payment-"+UUID.randomUUID().toString()
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

