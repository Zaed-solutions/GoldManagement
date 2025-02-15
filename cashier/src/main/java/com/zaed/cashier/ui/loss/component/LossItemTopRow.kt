package com.zaed.cashier.ui.loss.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.Loss
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun LossItemTopRow(
    date: String,
    losses: List<Loss>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date.format(DateFormat.DATE),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 8.dp, top = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = losses.sumOf { it.value }.toMoneyFormat(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 8.dp, top = 8.dp)
        )
    }
}