package com.zaed.common.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Date

@Composable
fun SaleSummaryContent(
    modifier: Modifier = Modifier,
    account: Account,
    products: List<Product>,
    totalAmount: Double,
    payWithGold: Boolean = false,
    isKaratUnSpecified: Boolean = false,
    totalMoneyPaid: Double,
    totalGoldPaid: Double = 0.0,
    onCreate: () -> Unit = {},
    isPurchase: Boolean = false,
    isLoading: Boolean = false,
    productType: ProductType,
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = if (isPurchase) stringResource(R.string.purchase_summary) else stringResource(R.string.sale_summary),
            style = MaterialTheme.typography.headlineMedium,
        )
        DetailRow(
            modifier = Modifier.padding(top = 16.dp),
            label = stringResource(R.string.name),
            value = account.name
        )
        DetailRow(
            label = stringResource(R.string.phone_number),
            value = account.phone
        )
        DetailRow(
            label = stringResource(R.string.created_at),
            value = Date().format(DateFormat.DATE),
            isDividerVisible = false
        )
        ProductsTable(
            products = products,
            isPurchase = isPurchase,
            isModifyEnabled = false,
            productType = productType
        )
        Spacer(modifier = Modifier.weight(1f))
        DashedDivider(
            thickness = 2.dp,
        )
        if (payWithGold) {
            PriceCalculationItem(
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                title = stringResource(R.string.gold_paid),
                price = totalGoldPaid
            )
        } else {
            PriceCalculationItem(
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                title = stringResource(R.string.money_paid),
                price = totalMoneyPaid
            )
        }
        PriceCalculationItem(
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            title = stringResource(R.string.future),
            price = if (isKaratUnSpecified) 0.0 else (totalAmount - totalMoneyPaid)
        )
        PriceCalculationItem(
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            title = stringResource(R.string.total),
            price = totalAmount
        )
        Button(
            onClick = onCreate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = !isLoading

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (payWithGold) {
                    Text(
                        text = stringResource(R.string.save_as_outstanding_bill),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        stringResource(R.string.process),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
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
    SaleSummaryContent(
        account = WholeSaleCustomer(
            id = "1",
            name = "محمد",
            phone = "1234567890",
            email = ""
        ),
        products = listOf(
            Product(
                id = "1",
                name = "منتج 1",
                grams = 100.0,
                gramPrice = 10.0
            ),
            Product(
                id = "2",
                name = "منتج 2",
                grams = 200.0,
                gramPrice = 20.0
            )
        ),
        totalAmount = 1253.0,
        totalMoneyPaid = 1243.0,
        productType = ProductType.GOLD
    )

}