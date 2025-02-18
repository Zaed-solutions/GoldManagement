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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.WholesaleGoldSale
import com.zaed.common.data.model.WholesaleProductSale
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.distributor.ui.theme.GoldenCustomColors

@Composable
fun GoldSaleItem(
    modifier: Modifier = Modifier,
    sale: WholesaleGoldSale,
    onSaleClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = GoldenCustomColors.current.color,
        contentColor = GoldenCustomColors.current.onColor,
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
        }
    }

}