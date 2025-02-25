package com.zaed.distributor.ui.ingottransactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.components.PriceCalculationItem
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: IngotTransaction,
) {
    Surface (
        modifier = modifier,
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = transaction.createdAt.format(DateFormat.DATE_TIME),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        com.zaed.common.R.string.ingot_transaction_title_placeholder,
                        transaction.grams,
                        transaction.karat.value
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = transaction.totalAmount.formatMoney(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
            PriceCalculationItem(
                style = MaterialTheme.typography.titleMedium,
                title = stringResource(R.string.total_earning),
                price = transaction.totalEarning
            )
        }
    }
}