package com.zaed.manager.ui.distributorssales.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.theme.GoldenCustomColors
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney

@Composable
fun DistributorSaleItem(
    modifier: Modifier = Modifier,
    sale: WholesaleTransaction,
    onClick: () -> Unit
) {
    val (icon, iconBackgroundColor, iconColor) = when{
        sale.productType == ProductType.GOLD -> Triple(R.drawable.ic_ingot, GoldenCustomColors.current.color, GoldenCustomColors.current.onColor)
        else -> Triple(R.drawable.ic_shopping, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = iconBackgroundColor,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = sale.createdAt.format(DateFormat.SHORT_DATE_TIME),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                    Text(
                        text = "DR-${sale.receiptNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = sale.distributorName,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
                Text(
                    text = sale.totalAmount.formatMoney(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}