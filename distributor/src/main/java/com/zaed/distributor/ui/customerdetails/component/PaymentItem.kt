package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.zaed.common.R
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.theme.DistributorAppTheme
import java.util.Date
import kotlin.math.absoluteValue

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    payment: Payment,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    SwipeToEditOrDeleteContainer(
        modifier = modifier,
        onDelete = onDelete,
        onEdit = onEdit,
        isEditEnabled = true,
    ) {
        Surface(
            modifier = Modifier.padding(8.dp),
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.medium
        ) {
            val chipColor =
                if (payment.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                        selected = true,
                        onClick = {},
                        label = { Text(text = payment.type.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor,
                            selectedLabelColor = contentColorFor(chipColor)
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if (payment.amount >= 0) stringResource(R.string.taken) else stringResource(R.string.given),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = payment.createdAt.format(DateFormat.TIME),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = payment.amount.absoluteValue.toMoneyFormat(2),
                        style = MaterialTheme.typography.titleLarge,
                        color = chipColor,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun PaymentItemPreview() {
    DistributorAppTheme {
    PaymentItem(
        payment = Payment(
            amount = 1000.0,
            type = com.zaed.common.data.model.payment.PaymentType.CASH,
            createdAt = Date(),
        )
    )
        }

}