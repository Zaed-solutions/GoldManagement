package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.R
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.components.DashedDivider
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PriceCalculationItem
import com.zaed.common.ui.components.ProductsTable
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Date

@Composable
fun SaleSummaryContent(
    modifier: Modifier = Modifier,
    sale: StoreSale,
    onSubmit: () -> Unit = {},
    isLoading: Boolean = false,
) {
    val discount = remember(sale) {
        sale.products.sumOf { it.discountAmount }
    }
    val subtotal = remember(sale) {
        sale.products.sumOf { it.totalPriceBeforeDiscount }
    }
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.sale_summary),
            style = MaterialTheme.typography.headlineMedium,
        )
        DetailRow(
            modifier = Modifier.padding(top = 16.dp),
            label = stringResource(R.string.name),
            value = sale.customerName
        )
        DetailRow(
            label = stringResource(R.string.phone_number),
            value = sale.customerPhone
        )
        DetailRow(
            label = stringResource(R.string.email),
            value = sale.customerEmail
        )
        DetailRow(
            label = stringResource(R.string.created_at),
            value = Date().format(DateFormat.DATE),
            isDividerVisible = false
        )
        ProductsTable(
            products = sale.products
        )
        Spacer(modifier = Modifier.weight(1f))
        DashedDivider(
            thickness = 2.dp,
        )
        PriceCalculationItem(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            title = stringResource(R.string.subtotal),
            price = subtotal
        )
        PriceCalculationItem(
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            title = stringResource(R.string.discount),
            isNegative = true,
            price = discount
        )
        PriceCalculationItem(
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            title = stringResource(R.string.total),
            price = sale.totalAmount
        )
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = !isLoading
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (isLoading) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    CashierAppTheme {
        val sale = StoreSale(
            discount = Discount(type = DiscountType.PERCENTAGE, value = 10.0),
            customerName = "Muhammed Edrees",
            customerEmail = "muhammed@edrees.com",
            customerPhone = "+20106476561",
            products = listOf(
                Product(
                    gramPrice = 10.0,
                    grams = 15.2
                ),
                Product(
                    gramPrice = 13.2,
                    grams = 10.8
                ),
                Product(
                    gramPrice = 18.5,
                    grams = 20.0
                ),
            )
        )
        SaleSummaryContent(
            modifier = Modifier.padding(vertical = 64.dp),
            sale = sale
        )
    }

}