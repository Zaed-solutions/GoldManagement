package com.zaed.distributor.ui.goldsaledetails.components

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
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.ui.util.formatMoney

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoldPaymentsTable(
    modifier: Modifier = Modifier,
    moneyPayments: List<MoneyPayment> = emptyList()
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
                            modifier = Modifier.weight(5f)
                        )
                        Text(
                            text = stringResource(R.string.value),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider()
                }
            }
            items(moneyPayments) { payment ->
                GoldPaymentTableItem(
                    moneyPayment = payment
                )
            }
        }
    }
}

@Composable
fun GoldPaymentTableItem(
    modifier: Modifier = Modifier,
    moneyPayment: MoneyPayment
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
                text = moneyPayment.type.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(5f)
            )
            Text(
                text = moneyPayment.amount.formatMoney(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.weight(2f)
            )
        }
    }

}