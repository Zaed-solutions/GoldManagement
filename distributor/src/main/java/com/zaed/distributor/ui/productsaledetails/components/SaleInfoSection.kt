package com.zaed.distributor.ui.productsaledetails.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.components.TitledSection
import java.util.Date
import com.zaed.common.R
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.FourTuple
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.distributor.ui.sales.PaymentStatus
import com.zaed.distributor.ui.sales.components.PaidStatusChip

@Composable
fun SaleInfoSection(
    modifier: Modifier = Modifier,
    receiptNumber: String,
    createdAt: Date,
    paid: Boolean,
    receiptStatus: ReceiptStatus,
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
            DetailRow(
                label = stringResource(R.string.payment_status),
                value = "",
                trailingContent = {
                    PaidStatusChip(
                        paid = paid
                    )
                },
                isDividerVisible = false,
            )
            DetailRow(
                label = stringResource(R.string.receipt_status),
                value = stringResource(receiptStatus.titleRes)
            )
            AnimatedVisibility(visible = receiptStatus == ReceiptStatus.NOT_REQUESTED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
        paid = true,
        receiptStatus = ReceiptStatus.PENDING
    )
}