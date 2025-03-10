package com.zaed.common.ui.saledetails.goldsaledetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PaidStatusChip
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Date

@Composable
fun GoldSaleInfoSection(
    modifier: Modifier = Modifier,
    receiptNumber: String,
    createdAt: Date,
    paymentStatus: PaymentStatus,
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
                        status = paymentStatus
                    )
                },
            )
            DetailRow(
                modifier = Modifier.padding(top = 8.dp),
                label = stringResource(R.string.receipt_status),
                value = stringResource(receiptStatus.titleRes),
                isDividerVisible = false,
            )
            AnimatedVisibility(visible = receiptStatus == ReceiptStatus.NOT_REQUESTED) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
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
