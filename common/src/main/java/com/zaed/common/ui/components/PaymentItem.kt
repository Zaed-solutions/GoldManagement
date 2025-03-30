package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.util.getPaymentTitle
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    payment: Payment,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCashed: () -> Unit = {},
    isEditable: Boolean = true,
    isDeletable: Boolean = true,
    canCashed: Boolean = false
) {
    val context = LocalContext.current
    val (type, subtitle) =
        when (payment) {
            is GoldPayment -> {
                stringResource(R.string.gold) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            is BankTransferPayment -> {
                stringResource(R.string.bank_transfer) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            is CashPayment -> {
                stringResource(R.string.cash) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            is ChequePayment -> {
                stringResource(R.string.cheque) to if (payment.chequeStatus == ChequeStatus.TRANSFERRED) {
                    stringResource(
                        R.string.transfered_to_template,
                        payment.receiverName
                    )
                } else if (payment.receiptNumber.isNotBlank()) {
                    stringResource(
                        R.string.receipt_number_template,
                        payment.receiptNumber
                    )
                } else ""
            }

            is ManagerCheque -> {
                stringResource(R.string.manager_cheque) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            is FuturePayment -> {
                stringResource(R.string.future) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            is LossPayment -> {
                stringResource(R.string.loss) to if (payment.receiptNumber.isNotBlank()) stringResource(
                    R.string.receipt_number_template,
                    payment.receiptNumber
                ) else ""
            }

            else -> {
                "" to ""
            }
        }

    val moreOptions = mutableListOf<MoreDropdownItem>()
    if (isEditable) {
        moreOptions.add(
            MoreDropdownItem(
                onClick = { onEdit() },
                icon = Icons.Default.Edit,
                title = context.getString(R.string.edit),
                tint = MaterialTheme.colorScheme.primary,
            ),
        )
    }
    if (canCashed && (payment is ChequePayment) && payment.chequeStatus == ChequeStatus.RECEIVED ) {
        moreOptions.add(
            MoreDropdownItem(
                onClick = { onCashed() },
                icon = Icons.Default.AttachMoney,
                title = context.getString(R.string.cashed),
                tint = MaterialTheme.colorScheme.primary,
            ),
        )
    }
    if (isDeletable) {
        moreOptions.add(
            MoreDropdownItem(
                onClick = { onDelete() },
                icon = Icons.Default.Delete,
                title = context.getString(R.string.delete),
                tint = MaterialTheme.colorScheme.error,
            )
        )
    }


    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (payment.given) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = payment.createdAt.getPaymentTitle(context),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = if (payment is GoldPayment ) stringResource(
                                R.string.grams_placeholder,
                                payment.givenGoldAmount.toString()
                            )else if (payment is FuturePayment&& payment.goldPayment){
                                stringResource(
                                R.string.grams_placeholder,
                                payment.amount.toString())
                            } else payment.amount.toMoneyFormat(),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (payment.given) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        if (moreOptions.isNotEmpty()) {
                            MoreDropDownMenu(
                                items = moreOptions,
                                isVertical = true
                            )
                        } else {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (subtitle.isNotBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            HorizontalDivider()
        }
    }
}








