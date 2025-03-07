package com.zaed.distributor.ui.addGoldSale.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.sale.WholesaleProductSale
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
    customer: WholeSaleCustomer,
    sale: WholesaleProductSale,
    cashPayments: List<CashPayment>,
    totalAmount: Double,
    totalPaid: Double,
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
            value = customer.name
        )
        DetailRow(
            label = stringResource(R.string.phone_number),
            value = customer.phone
        )
        DetailRow(
            label = stringResource(R.string.created_at),
            value = Date().format(DateFormat.DATE),
            isDividerVisible = false
        )
        ProductsTable(
            products = sale.products,
            isModifyEnabled = false,
            onEditProduct = {},
            onRemoveProduct = {}
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
            title = stringResource(R.string.paid),
            price = totalPaid
        )
        PriceCalculationItem(
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            title = stringResource(R.string.future),
            price = totalAmount - totalPaid
        )
        PriceCalculationItem(
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            title = stringResource(R.string.total),
            price = totalAmount
        )
    }

}