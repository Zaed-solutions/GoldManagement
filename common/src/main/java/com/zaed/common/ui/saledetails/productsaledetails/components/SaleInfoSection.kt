package com.zaed.common.ui.saledetails.productsaledetails.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.ReceiptStatus
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.PaidStatusChip
import com.zaed.common.ui.components.PrinterBottomSheet
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import java.util.Date

@Composable
fun SaleInfoSection(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    receiptNumber: String,
    createdAt: Date,
    totalPrice: Double,
    paymentStatus: PaymentStatus = PaymentStatus.PAID,
    receiptStatus: ReceiptStatus? = null,
    onRequestReceipt: () -> Unit = {},
    isPurchase: Boolean = false,
) {
    var isPrinterSheetVisible by remember { mutableStateOf(false) }
    TitledSection(
        modifier = modifier,
        title = if (isPurchase) stringResource(R.string.purchase_information) else stringResource(R.string.sale_information),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (!isPurchase) {
                    OutlinedButton(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            isPrinterSheetVisible = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.print))
                    }
                }
                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    enabled = receiptStatus == ReceiptStatus.NOT_REQUESTED,
                    onClick = {
                        onRequestReceipt()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    AnimatedContent(receiptStatus) { state ->
                        when (state) {
                            ReceiptStatus.NOT_REQUESTED -> {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.request_receipt),
                    )
                }
            }
            PrinterBottomSheet(
                isVisible = isPrinterSheetVisible,
                onDismiss = { isPrinterSheetVisible = false },
                sale = transaction
            )
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
        receiptStatus = ReceiptStatus.PENDING,
        transaction = WholesaleTransaction()
    )
}