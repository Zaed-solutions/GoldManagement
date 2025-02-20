package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.theme.DistributorAppTheme
import kotlin.math.absoluteValue

@Composable
fun BalanceSection(
    modifier: Modifier = Modifier,
    amount: Double,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = amount.getDebtTitle(),
                style = MaterialTheme.typography.titleLarge,

                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = amount.getContainerColor(),
                shape = RoundedCornerShape(8f.dp)
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    text = amount.absoluteValue.toMoneyFormat(2),
                    style = MaterialTheme.typography.titleLarge,
                    color = amount.getContentColor(),
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun BalanceSectionPreview() {
    DistributorAppTheme {
        BalanceSection(amount = 1000.0)
    }
}

@Composable
fun Double.getContainerColor() =
    when {
        this >= 0 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }

@Composable
fun Double.getContentColor() =
    when {
        this >= 0 -> contentColorFor(MaterialTheme.colorScheme.primary)
        else -> contentColorFor(MaterialTheme.colorScheme.error)
    }

@Composable
fun Double.getDebtTitle() =
    when {
        this >= 0 -> stringResource(com.zaed.common.R.string.not_in_debt)
        else -> stringResource(com.zaed.common.R.string.in_debt)
    }