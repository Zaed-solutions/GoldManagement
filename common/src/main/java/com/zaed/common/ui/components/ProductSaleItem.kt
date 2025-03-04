package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney
import java.util.Date

@Composable
fun ProductSaleItem(
    modifier: Modifier = Modifier,
    sale: WholesaleProductSale,
    onSaleClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = { onSaleClicked() },
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Money,
                    contentDescription = null,
                )
                Column (
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ){
                    Text(
                        text = "DR-${sale.receiptNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = sale.createdAt.format(DateFormat.TIME),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
                Text(
                    text = sale.totalPrice.formatMoney(),
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    ProductSaleItem(
        sale = WholesaleProductSale(
            receiptNumber = "123456",
            createdAt = Date(),
            products = listOf(
                Product(
                    name = "Product 1",
                    quantity = 1,
                    grams = 10.0,
                    gramPrice = 100.0
                ),
                Product(
                    name = "Product 2",
                    quantity = 2,
                    grams = 20.0,
                    gramPrice = 200.0
                ),
                Product(
                    name = "Product 3",
                    quantity = 3,
                    grams = 30.0,
                    gramPrice = 300.0
                )
            )
        ),
        onSaleClicked = {}
    )
}