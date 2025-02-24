package com.zaed.distributor.ui.addGoldSale.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField

@Composable
fun SaveGoldPaymentBottomSheetContent(
    modifier: Modifier = Modifier,
    initialMoneyPayment: Payment,
    onSave: (Payment) -> Unit = {}
) {

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(PaymentType.CASH,PaymentType.GOLD)
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
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(top = 8.dp),
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex
                ) {
                    Text(label.name)
                }
            }
        }
        AnimatedContent(selectedIndex) { it ->
            when (it) {
                0 -> {
                    var payment by remember { mutableStateOf(initialMoneyPayment as? MoneyPayment?: MoneyPayment()) }
                    Column {
                        TitledDropDownTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            label = stringResource(R.string.type),
                            options = PaymentType.entries.map { it.name },
                            selectedValue = payment.type.name,
                            onValueChanged = { index ->
                                payment =
                                    payment.copy(type = PaymentType.entries[index])
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
                1->{
                    var payment by remember { mutableStateOf(initialMoneyPayment as? GoldPayment?: GoldPayment()) }
                    Column {
                        TitledDropDownTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            label = stringResource(R.string.type),
                            options = PaymentType.entries.map { it.name },
                            selectedValue = payment.type.name,
                            onValueChanged = { index ->
                                payment =
                                    payment.copy(type = PaymentType.entries[index])
                            },
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
