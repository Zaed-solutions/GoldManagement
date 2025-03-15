package com.zaed.distributor.ui.sales.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.WholesaleProductTransaction
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.theme.DistributorAppTheme

@Composable
fun ProductSaleItem(
    modifier: Modifier = Modifier,
    sale: WholesaleProductTransaction,
    onSaleClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = { onSaleClicked() },
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Box (
                modifier
                    .padding(end = 4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

            ){
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Column(
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = sale.createdAt.format(DateFormat.SHORT_DATE_TIME),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = sale.totalPriceBeforeDiscount.toMoneyFormat(2),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = sale.toPreview(),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductSaleItemPreview() {
    DistributorAppTheme {
        ProductSaleItem(
            sale = WholesaleProductTransaction(
                customerName = "Ali",
                createdAt = java.util.Date(),
                products = listOf(
                    com.zaed.common.data.model.sale.Product(
                        name = "Gold",
                        quantity = 5,
                        gramPrice = 100.0,
                        grams = 10.0
                    ),
                    com.zaed.common.data.model.sale.Product(
                        name = "Silver",
                        quantity = 5,
                        gramPrice = 100.0,
                        grams = 10.0
                    ),
                    com.zaed.common.data.model.sale.Product(
                        name = "Platinum",
                        quantity = 5,
                        gramPrice = 100.0,
                        grams = 10.0
                    ),
                    com.zaed.common.data.model.sale.Product(
                        name = "Diamond",
                        quantity = 5,
                        gramPrice = 100.0,
                        grams = 10.0
                    )
                ),
            ),
            onSaleClicked = {}
        )
    }

}

fun WholesaleProductTransaction.toPreview() =
    "$customerName - ${products.joinToString { "${it.quantity} ${it.name}" }.substringBeforeLast(",")}"
