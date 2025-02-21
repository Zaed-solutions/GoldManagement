package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.components.DashedDivider
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PriceCalculationItem
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Date

@Composable
fun SaleSummaryContent(
    modifier: Modifier = Modifier,
    sale: StoreSale,
) {
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
            value = sale.customerPhoneNumber
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
        Spacer(modifier = Modifier.weight(1f))
        DashedDivider(
            thickness = 2.dp,
        )
        if(sale.discount.type != DiscountType.NONE){
            PriceCalculationItem(
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal, fontSize = 18.sp),
                title = stringResource(R.string.subtotal),
                price = sale.products.sumOf { it.gramPrice * it.grams }
            )
            PriceCalculationItem(
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal, fontSize = 18.sp),
                title = stringResource(R.string.discount),
                isNegative = true,
                price = when(sale.discount.type){
                    DiscountType.NONE -> 0.0
                    DiscountType.PERCENTAGE -> sale.products.sumOf { it.grams * it.gramPrice } * (sale.discount.value/100.0)
                    DiscountType.AMOUNT -> sale.discount.value
                }
            )

        }
        PriceCalculationItem(
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            title = stringResource(R.string.total),
            price = sale.totalPrice
        )
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
            customerPhoneNumber = "+20106476561",
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
            sale = sale)
    }

}