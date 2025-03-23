package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.manager.R

@Composable
fun SummaryCards(
    totalProfit: Double,
    totalLoss: Double,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryCard(
            title = stringResource(R.string.gross_profit),
            value = totalProfit,
            isTrending = true,
            backgroundColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = stringResource(R.string.total_loss),
            value = totalLoss,
            isTrending = false,
            backgroundColor = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f)
        )
    }
}