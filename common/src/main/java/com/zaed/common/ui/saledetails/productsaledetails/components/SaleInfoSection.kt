package com.zaed.common.ui.saledetails.productsaledetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PaidStatusChip
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import java.util.Date

@Composable
fun SaleInfoSection(
    modifier: Modifier = Modifier,
    receiptNumber: String,
    createdAt: Date,
    totalPrice: Double,
    paymentStatus: PaymentStatus = PaymentStatus.PAID,
    receiptStatus: ReceiptStatus? = null,
    onRequestReceipt: () -> Unit = {}
) {
    TitledSection(
        modifier = modifier,
        title = stringResource(R.string.sale_information),
    ) {
        Column {
            DetailRow(
                label = stringResource(R.string.receipt_number),
                value = receiptNumber
            )
            DetailRow(
                modifier = Modifier.padding(top = 8.dp),
                label = stringResource(R.string.created_at),
                value = createdAt.format(DateFormat.DATE_TIME)
            )
            Text(
                text = totalPrice.toMoneyFormat(2),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            DetailRow(
                label = stringResource(R.string.payment_status),
                value = "",
                trailingContent = {
                    PaidStatusChip(
                        status = paymentStatus
                    )
                },
            )
            if (receiptStatus != null) {
                DetailRow(
                    modifier = Modifier.padding(top = 8.dp),
                    label = stringResource(R.string.receipt_status),
                    value = stringResource(receiptStatus.titleRes),
                    isDividerVisible = false,
                )
            }
            AnimatedVisibility(visible = receiptStatus == ReceiptStatus.NOT_REQUESTED) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            onRequestReceipt()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.request_receipt),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    SaleInfoSection(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 50.dp),
        receiptNumber = "123456",
        createdAt = Date(),
        paymentStatus = PaymentStatus.PAID,
        totalPrice = 100.0,
        receiptStatus = ReceiptStatus.PENDING
    )
}