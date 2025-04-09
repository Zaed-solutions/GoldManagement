package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.components.FadedVerticalDivider
import com.zaed.manager.ui.theme.ManagerTheme

@Composable
fun HomeSummaryItem(
    modifier: Modifier = Modifier,
    summary: HomeSummary
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.large,
        onClick = summary.onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(summary.iconRes),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IncomeExpenseCardSection(
                        title = stringResource(R.string.total_sales),
                        amount = summary.totalSales,
                        color = MaterialTheme.colorScheme.onSurface,
                        colorVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                        horizontalAlignment = Alignment.Start
                    )
                    if (summary.totalLosses > 0) {
                        FadedVerticalDivider(
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IncomeExpenseCardSection(
                            title = stringResource(R.string.total_loss),
                            amount = summary.totalLosses,
                            color = MaterialTheme.colorScheme.onSurface,
                            colorVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                            horizontalAlignment = Alignment.Start
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    ManagerTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) { }
        HomeSummaryItem(
            modifier = Modifier.padding(vertical = 16.dp),
            summary = HomeSummary(title = stringResource(R.string.stores),
                iconRes = R.drawable.ic_store,
                totalSales = 1000.0,
                totalLosses = 100.0,
                onClick = {}
            )
        )
    }
}