package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.ui.components.FadedVerticalDivider
import com.zaed.common.ui.util.formatMoney
import com.zaed.manager.ui.theme.ManagerTheme

@Composable
fun EarningsAndLossesHeader(
    modifier: Modifier = Modifier,
    totalEarnings: Double,
    totalLosses: Double
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IncomeExpenseCardSection(title = stringResource(com.zaed.common.R.string.total_earning), amount = totalEarnings)
            FadedVerticalDivider(modifier = Modifier.padding(horizontal = 40.dp))
            IncomeExpenseCardSection(
                title = stringResource(com.zaed.common.R.string.total_loss),
                amount = totalLosses
            )
        }
    }
}

@Composable
fun IncomeExpenseCardSection(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    colorVariant: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = colorVariant
        )
        Text(
            text = amount.formatMoney(),
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun IncomeAndExpensesCardPreview() {
    ManagerTheme {
        EarningsAndLossesHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            totalEarnings = 5440.0,
            totalLosses = 2209.0
        )
    }
}