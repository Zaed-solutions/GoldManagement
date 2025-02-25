package com.zaed.distributor.ui.addGoldSale.components

import androidx.compose.animation.AnimatedContent
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
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getPaymentTypeDropDownItems
import com.zaed.common.ui.components.MultiOptionSwitch
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField

@Composable
fun SaveGoldPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialMoneyPayment: Payment,
    onSave: (Payment) -> Unit = {}
) {

    var selectedType by remember { mutableStateOf(initialMoneyPayment.type) }
    val options = listOf(PaymentType.CASH, PaymentType.GOLD)
    val dropDownOptions = remember {
        getPaymentTypeDropDownItems()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.save_payment),
            style = MaterialTheme.typography.titleLarge
        )
        MultiOptionSwitch(
            modifier = Modifier.padding(top = 8.dp),
            selectedIndex = options.indexOf(selectedType),
            options = options.map { stringResource(it.titleRes) },
            onOptionSelected = { index ->
                selectedType = options[index]
            }
        )
        AnimatedContent(selectedType) { it ->
            when (it) {
                PaymentType.CASH -> {
                    var payment by remember {
                        mutableStateOf(
                            initialMoneyPayment as? MoneyPayment ?: MoneyPayment()
                        )
                    }
                    Column {
                        TitledDropDownTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            label = stringResource(R.string.type),
                            options = dropDownOptions.map { stringResource(it.titleRes) },
                            selectedValue = stringResource(payment.type.titleRes),
                            onValueChanged = { index ->
                                payment =
                                    payment.copy(type = dropDownOptions[index])
                            },
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
                                onSave(payment)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.save),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                else -> {
                    var payment by remember {
                        mutableStateOf(
                            initialMoneyPayment as? GoldPayment ?: GoldPayment()
                        )
                    }
                    Column {

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
                                onSave(payment)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.save),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
private fun PaymentTypeDropDownMenu(
    modifier: Modifier = Modifier,
    moneyPayment: MoneyPayment,
    onTypeChanged: (PaymentType) -> Unit
) {
    TitledDropDownTextField(
        modifier = modifier,
        label = stringResource(R.string.type),
        options = PaymentType.entries.map { it.name },
        selectedValue = moneyPayment.type.name,
        onValueChanged = { index ->
            onTypeChanged(PaymentType.entries[index])
        },
    )
}

fun Int.ifZero(value: () -> String): String = if (this == 0) value() else this.toString()
