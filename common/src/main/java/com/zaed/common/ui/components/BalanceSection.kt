package com.zaed.common.ui.components

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
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.util.toMoneyFormat
import kotlin.math.absoluteValue

@Composable
fun BalanceSection(
    modifier: Modifier = Modifier,
    isSupplier: Boolean = false,
    amount: Double,
    isGold: Boolean = false,
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
                text = amount.getDebtTitle(isSupplier),
                style = MaterialTheme.typography.titleLarge,

                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = amount.getContainerColor(isSupplier),
                shape = RoundedCornerShape(8f.dp)
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    text = if(isGold)
                        stringResource(
                            R.string.grams_placeholder,
                            amount.absoluteValue.toString()
                        ) else amount.absoluteValue.toMoneyFormat(2),
                    style = MaterialTheme.typography.titleLarge,
                    color = amount.getContentColor(isSupplier),
                )
            }
        }
    }
}



@Composable
fun Double.getContainerColor(isSupplier: Boolean = false) =
    when {
        isSupplier || this >= 0  -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }

@Composable
fun Double.getContentColor(isSupplier: Boolean = false) =
    when {
        isSupplier || this >= 0 -> contentColorFor(MaterialTheme.colorScheme.primary)
        else -> contentColorFor(MaterialTheme.colorScheme.error)
    }

@Composable
fun Double.getDebtTitle(isSupplier: Boolean = false) =
    when {
        isSupplier -> stringResource(R.string.credit)
        this >= 0 -> stringResource(com.zaed.common.R.string.not_in_debt)
        else -> stringResource(com.zaed.common.R.string.in_debt)
    }