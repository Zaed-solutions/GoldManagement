package com.zaed.common.ui.saledetails.productsaledetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentsTable(
    modifier: Modifier = Modifier,
    payments: List<Payment> = emptyList()
) {
    Surface(
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),

            ),
        shape = MaterialTheme.shapes.medium
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
        ) {
            // Header
            stickyHeader {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.type),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(3f)
                        )
                        Text(
                            text = stringResource(R.string.value),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(5f),
                            textAlign = TextAlign.End
                        )
                    }
                    HorizontalDivider()
                }
            }
            items(payments) { payment ->
                PaymentTableItem(
                    payment = payment
                )
            }
        }
    }
}

@Composable
fun PaymentTableItem(
    modifier: Modifier = Modifier,
    payment: Payment
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(payment.type.titleRes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(3f)
            )

            Text(
                text = if (payment is GoldPayment ) stringResource(
                    R.string.grams_placeholder,
                    payment.givenGoldAmount.toString()
                )else if (payment is FuturePayment && payment.goldPayment){
                    stringResource(
                        R.string.grams_placeholder,
                        payment.amount.toString())
                } else payment.amount.toMoneyFormat(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.weight(5f)
            )
        }
    }

}