package com.zaed.distributor.ui.sales.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.components.FourTuple
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.distributor.ui.sales.PaymentStatus

@Composable
fun ProductSaleItem(
    modifier: Modifier = Modifier,
    sale: WholesaleProductSale,
    onSaleClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { onSaleClicked() },
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = sale.customerName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                )
                Text(
                    text = sale.createdAt.format(DateFormat.DATE_TIME),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                )
            }
            PaidStatusChip(
                paid = sale.paid
            )
        }
    }
}

@Composable
fun PaidStatusChip(
    modifier: Modifier = Modifier,
    paid: Boolean
) {
    val (label, borderColor, backgroundColor, textColor) = when {
        paid -> FourTuple(
            stringResource(PaymentStatus.PAID.label),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )
        else -> FourTuple(
            stringResource(PaymentStatus.UNPAID.label),
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.tertiary
        )
    }
    AssistChip(
        modifier = modifier,
        onClick = {},
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor,
            labelColor = textColor
        ),
        shape = MaterialTheme.shapes.small,
        label = {
            Text(
                text = label
            )
        },
    )
}