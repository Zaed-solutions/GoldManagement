package com.zaed.common.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.Sale
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.sale.WholesaleGoldSale
import com.zaed.common.ui.theme.GoldManagementTheme
import com.zaed.common.ui.theme.GoldenCustomColors
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney
import java.util.Date

@Composable
fun SaleItem(
    modifier: Modifier = Modifier,
    sale: Sale,
    onSaleClicked: () -> Unit,
    isDividerVisible: Boolean = true
) {
    val (icon, iconBackgroundColor, iconColor) = when{
        sale is WholesaleGoldSale -> Triple(R.drawable.ic_ingot, GoldenCustomColors.current.color, GoldenCustomColors.current.onColor)
        else -> Triple(R.drawable.ic_shopping, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
    }
    val title = when{
        sale is StoreSale -> "CR-${sale.receiptNumber}"
        else -> "DR-${sale.receiptNumber}"
    }
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
                Column (
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = sale.createdAt.format(DateFormat.SHORT_DATE_TIME),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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
            if(isDividerVisible) {
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    GoldManagementTheme {
        SaleItem(
            sale = WholesaleGoldSale(
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
}