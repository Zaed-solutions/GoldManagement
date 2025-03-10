package com.zaed.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.payment.PaymentStatus

@Composable
fun PaidStatusChip(
    modifier: Modifier = Modifier,
    status: PaymentStatus
) {
    val (label, borderColor, backgroundColor, textColor) = when (status) {
        PaymentStatus.PAID -> FourTuple(
            stringResource(status.label),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )

        PaymentStatus.UNPAID -> FourTuple(
            stringResource(status.label),
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.secondary
        )

        else -> FourTuple(
            stringResource(status.label),
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