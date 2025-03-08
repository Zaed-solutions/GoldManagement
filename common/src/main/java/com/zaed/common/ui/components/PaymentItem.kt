package com.zaed.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat
import kotlin.math.absoluteValue

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    payment: Payment,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    when (payment) {
        is CashPayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =
                        if (payment.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (payment.amount >= 0) stringResource(R.string.taken) else stringResource(
                                    R.string.given
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = payment.amount.absoluteValue.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
        is LossPayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =MaterialTheme.colorScheme.error
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "خسارة مبيعات"
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = payment.amount.absoluteValue.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
        is FuturePayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =MaterialTheme.colorScheme.error
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "مدفوعات آجله"
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = payment.amount.absoluteValue.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
        is GoldPayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =
                        if (payment.givenGoldAmount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    val tag1 = if (payment.givenGoldAmount >= 0) stringResource(R.string.taken) else stringResource(R.string.given)
                    val tag2 = if(payment.pricePerGram == 0.0) stringResource(R.string.not_specified_yet) else ""
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text ="$tag1 $tag2"   ,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(
                                    R.string.grams_placeholder,
                                    payment.givenGoldAmount.absoluteValue.toString()
                                ) ,
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
        is ChequePayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =
                        if (payment.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (payment.amount >= 0) stringResource(R.string.taken) else stringResource(
                                    R.string.given
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text =
                                    payment.amount.toMoneyFormat(2)
                                ,
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
        is BankTransferPayment -> {
            SwipeToEditOrDeleteContainer(
                modifier = modifier,
                onDelete = onDelete,
                onEdit = onEdit,
                isEditEnabled = true,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    onClick = onClick,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    val chipColor =
                        if (payment.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                                selected = true,
                                onClick = {},
                                label = { Text(text = stringResource(payment.type.titleRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = contentColorFor(chipColor)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (payment.amount >= 0) stringResource(R.string.taken) else stringResource(
                                    R.string.given
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.createdAt.format(DateFormat.TIME),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = payment.amount.absoluteValue.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                color = chipColor,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }

    }
}
